package com.trabajo.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.trabajo.utils.IOUtils;

public class ServicePropertiesHandler {
  private Properties p=new Properties();
    
    
    public ServicePropertiesHandler(File tmpDir, Path path) {
        try {
        		if(path.toFile().exists()) {
	            File tmp=File.createTempFile("service", ".properties", tmpDir);
	            IOUtils.byteArrayToFile(IOUtils.bytesFromURL(path.toFile().toURI().toURL()), tmp);
	            
	            JarFile jf=new JarFile(tmp);
	            JarEntry je=jf.getJarEntry("META-INF/service.properties");
	            InputStream inStream=jf.getInputStream(je);
							p.load(inStream);
							inStream.close();
	            jf.close();
	            
							tmp.delete();
        		}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties getServiceProperties() {
        return p;
    }
}
