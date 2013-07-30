package com.trabajo.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.trabajo.ValidationException;
import com.trabajo.annotation.AnnotationQueryClassLoader;
import com.trabajo.utils.IOUtils;

public class ProcessJar {
	private File file;
	private Manifest manifest;
	private ProcessClassMetadata pcm;
	private String[] dependencies;
	
	public ProcessJar(File file) throws ValidationException {
		this.file = file;
		
		try(AnnotationQueryClassLoader aqcl=new AnnotationQueryClassLoader(); JarFile jf = new JarFile(file)) {
			manifest=jf.getManifest();
			if(manifest==null) {
				throw new ValidationException("Manifest missing: requires need to supply entry for 'Trabajo-Process-Class'");
			}
			byte[] bytes;
			try{
				bytes = IOUtils.classBytesFromJar(getProcessClassName(), jf);
			}catch(NullPointerException ee){
				throw new ValidationException("No class found for 'Trabajo-Process-Class' are you sure the jar was  packaged correctly");
			}
			Class<?> processClass=aqcl.define(getProcessClassName(), bytes);
			pcm=new ProcessClassMetadata(processClass);
		} catch (IOException e) {
			throw new ValidationException("invalid jar file - cannot read: "+file.getAbsolutePath());
		}
	}
	
	public ProcessJar(byte[] jarBytes) throws ValidationException {

		
		try(AnnotationQueryClassLoader aqcl=new AnnotationQueryClassLoader()) {
			final File localWorkDir = new File(System.getProperty("user.home")+ File.separatorChar + ".trabajo");
			final File localWorkProcessTempDir = new File(localWorkDir,"process_temp");

			if (!localWorkDir.exists()) {
				localWorkDir.mkdirs();
			}

			if (!localWorkProcessTempDir.exists()) {
				localWorkProcessTempDir.mkdirs();
			}

			file = File.createTempFile("tempProcess", ".jar", localWorkProcessTempDir);
			IOUtils.byteArrayToFile(jarBytes, file);
			JarFile jf = new JarFile(file);
			manifest=jf.getManifest();
			
			pcm=new ProcessClassMetadata(aqcl.define(getProcessClassName(), IOUtils.classBytesFromJar(getProcessClassName(), jf)));
			file.delete();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public File getFile() {
		return file;
	}

	public String getProcessClassName()  {
		return manifest.getMainAttributes().getValue("Trabajo-Process-Class");
	}

	public ProcessClassMetadata getClassMetadata()  {
		return pcm;
	}

	public InputStream getJarInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public void setDependencies(String[] urls) {
		this.dependencies=urls;
	}


	public String[] getDependencies() {
		return dependencies;
	}

}
