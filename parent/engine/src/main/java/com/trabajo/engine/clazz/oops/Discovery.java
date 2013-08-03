package com.trabajo.engine.clazz.oops;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Discovery {
    private String name;
    private JarFile jf;
    private JarEntry je;
    
    private static List<JarFile> jars=new ArrayList<>();
    
    public Discovery(String name, JarFile jf, JarEntry je) {
        super();
        this.name = name;
        this.jf = jf;
        this.je = je;
    }
    
    public Discovery(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public InputStream getStream() throws IOException {
        if(jf==null) {
            for(JarFile j: jars) {
                JarEntry ge;
                try{
                    ge=jf.getJarEntry(name+".class");
                    
                    
                }catch(Exception e){
                    if(name.startsWith("p.")) {
                        System.out.println("==============================> "+name);
                    }
                    return null;
                }
                if(ge!=null) {
                    this.jf=j;
                    this.je=ge;
                    return j.getInputStream(ge);
                }
            }
        }
        return jf.getInputStream(je);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Discovery other = (Discovery) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public static void addJar(JarFile jarFile) {
        synchronized(jars) {
            jars.add(jarFile);
        }
    }
    
    
}
