package com.trabajo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IOUtils {

	public static final int COPY_BUFFER_SIZE = 1024;

	public static void copyStream(InputStream is, OutputStream os) throws IOException {

		byte[] buf = new byte[COPY_BUFFER_SIZE];

		for (int i = 0; (i = is.read(buf)) != (-1); os.write(buf, 0, i))
			;

		is.close();
		os.close();
	}

	public static void copyStream(InputStream is, OutputStream os, boolean closeOs) throws IOException {

		byte[] buf = new byte[COPY_BUFFER_SIZE];

		for (int i = 0; (i = is.read(buf)) != (-1); os.write(buf, 0, i))
			;

		is.close();
		if(closeOs) {
			os.close();
		}
	}

	public static byte[] bytesFromStream(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copyStream(is, baos);
		return baos.toByteArray();
	}

	public static byte[] bytesFromResource(String resource) throws IOException {
		return bytesFromStream(streamFromResource(resource));
	}

	public static InputStream streamFromResource(String resource) throws IOException {
		return new BufferedInputStream(IOUtils.class.getResourceAsStream(resource));
	}
    
	public static InputStream streamFromString(String data) throws IOException {
		return new ByteArrayInputStream(data.getBytes("UTF8"));
  }


	public static String stringFromUTF8Stream(InputStream inStream) throws IOException {
		return stringFromStream(inStream, "UTF-8");
	}

	public static String stringFromStream(InputStream inStream, String encoding) throws IOException {
		try {
			return new String(bytesFromStream(inStream), encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static JarEntry entry(final String jarEntryName, final JarFile zf) {
		Enumeration<? extends JarEntry> entries = zf.entries();
		JarEntry ze = null;

		while (entries.hasMoreElements()) {
			ze = entries.nextElement();

			if (ze.isDirectory()) {
				continue;
			}

			if (ze.getName().equals(jarEntryName)) {
				return ze;
			}
		}

		return null;
	}

	public static byte[] classBytesFromJar(final String dotQualifiedClassName, final JarFile jf) {

		try {
			String converted= dotQualifiedClassName.replace('.', '/')+".class";
			JarEntry je=entry(converted, jf);
			return bytesFromStream(jf.getInputStream(je));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static InputStream entryStream(final String jarEntryName, final JarFile zf) {
		try {
			return zf.getInputStream(entry(jarEntryName, zf));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void moveTo(File f, File destDir) throws IOException {
		File dest = new File(destDir, f.getName());

		if (dest.exists()) {
			dest.delete();
		}

		FileInputStream fis = new FileInputStream(f);
		File destDirFile = new File(destDir, f.getName());
		FileOutputStream fos = new FileOutputStream(destDirFile);

		copyStream(fis, fos);
		f.delete();
	}

	public static void copyFile(File f, File dest) throws IOException {
		FileInputStream fis = new FileInputStream(f);

		FileOutputStream fos = new FileOutputStream(dest);

		copyStream(fis, fos);
	}

	public static void copyStreamClosing(InputStream is, OutputStream os, boolean closeIn, boolean closeOut) throws IOException {
		byte[] buf = new byte[COPY_BUFFER_SIZE];

		for (int i = 0; (i = is.read(buf)) != (-1); os.write(buf, 0, i))
			;

		if(closeIn) {
			is.close();
		}
		if(closeOut) {
			os.close();
		}
	}

	public static OutputStream bufferedFileOutStream(File file) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(file));
	}

	public static InputStream bufferedFileInStream(File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}

	public static File tmpFileFromByteArry(File tmpDir, String tmpFilePrefix, byte[] fileBytes) throws IOException {
		File tmp=File.createTempFile(tmpFilePrefix, "tmp", tmpDir);
		IOUtils.copyStream(new ByteArrayInputStream(fileBytes), new BufferedOutputStream(new FileOutputStream(tmp)));
		return tmp;
	}

	public static void byteArrayToFile(byte[] data, File file) throws FileNotFoundException, IOException {
		copyStream(new ByteArrayInputStream(data), bufferedFileOutStream(file));
	}

	public static byte[] bytesFromURL(URL url) throws IOException {
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		copyStream(url.openStream(), baos);
		return baos.toByteArray();
	}

}
