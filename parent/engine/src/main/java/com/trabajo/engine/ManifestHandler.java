package com.trabajo.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.trabajo.process.ServiceInfo;
import com.trabajo.utils.IOUtils;

public class ManifestHandler {
    private Manifest m;
    
    
    public ManifestHandler(File tmpDir, Path path) {
        try {
            File tmp=File.createTempFile("manifest-query", ".jar", tmpDir);
            IOUtils.byteArrayToFile(IOUtils.bytesFromURL(path.toFile().toURI().toURL()), tmp);
            JarFile jf=new JarFile(tmp);
            m=jf.getManifest();
            jf.close();
            tmp.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServiceFactoryClassName() {
        return m==null ? null:m.getMainAttributes().getValue(ServiceInfo.PROP_IMPL_FACTORY_CLASS);
    }
}
