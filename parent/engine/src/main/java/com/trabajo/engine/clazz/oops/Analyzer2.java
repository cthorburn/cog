/*
Copyright (c) 2007 Greg Vanore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package com.trabajo.engine.clazz.oops;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.trabajo.utils.Strings;

public class Analyzer2 {
    
    private Analysis analysis =new Analysis();       
            
    public static class Analysis {
        private final Map<String, JarAnalysis> jars = new HashMap<>();

        public JarAnalysis jarAnalysis(String jarName) {
            JarAnalysis ja=jars.get(jarName);
        
            if(ja==null) {
                ja=new JarAnalysis(jarName);
                jars.put(jarName, ja);
            }
            return ja;
        }

        public Set<String> asFixList() {
            Set<String> result=new HashSet<>();
            
            
            for(JarAnalysis ja: jars.values()) {
                for(PackageAnalysis pa: ja.packages.values()) {
                    result.addAll(pa.fix);
                }
            }
            return result;
        }
        
        @Override
        public String toString() {
            StringBuilder sb=new StringBuilder();
            int ok=0;
            int fix=0;
            
            for(JarAnalysis ja: jars.values()) {
                sb.append(ja.name);
                sb.append('\n');
                
                for(PackageAnalysis pa: ja.packages.values()) {
                    sb.append("   "+pa.name+" ok: "+pa.ok.size()+" fix: "+pa.fix.size()+"\n");
                    ok+=pa.ok.size();
                    fix+=pa.fix.size();
                }
            }
            sb.append("all:  ok: "+ok+" fix: "+fix+" %: "+((fix*100)/(fix+ok))+"\n");
            return sb.toString();
        }
    }
    
    public static class JarAnalysis {
        private String name;
        Map<String, PackageAnalysis> packages = new HashMap<>();
        
        public JarAnalysis(String name) {
            this.name=name;
        }

        public void add(String c, boolean ok) {
            String p=Strings.beforeLast(c, '/');
            String z=Strings.afterLast(c, '/');
            PackageAnalysis pa=packages.get(p);
            if(pa==null) {
                pa=new PackageAnalysis(p);
                packages.put(p, pa);
            }
            if(ok) {
                pa.ok.add(z);
            }
            else {
                pa.fix.add(z);
            }
        }
    }
    
    public static class PackageAnalysis {
        String name;
        List<String> ok=new ArrayList<>();
        List<String> fix=new ArrayList<>();

        public PackageAnalysis(String name) {
            this.name=name;
        }
    }
    
    private static final Pattern CLSID = Pattern.compile("\\[*?L(.*?(/.*?)*);");

    protected final ClassReferenceFinder CLS_FINDER = new ClassReferenceFinder();

    protected static String extractClass(String desc) {
        Matcher m = CLSID.matcher(desc);
        if (m.matches()) {
            if (m.group(1).length() == 1)
                return null;
            return m.group(1);
        }
        return null;
    }


    public Analyzer2(File[] jars) {

        for (File f: jars) {
            if (!f.exists() || !f.canRead()) {
                throw new RuntimeException("unable to read: "+f.getAbsolutePath());
            }    

            try {
                processJarFile(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processJarFile(File jarFile) throws IOException {
        String jarName=jarFile.getName();
        
        JarAnalysis ja=analysis.jarAnalysis(jarName);
        
        try (JarFile jf = new JarFile(jarFile)) {
          JarEntry je = null;
          Enumeration<JarEntry> jarEntries = jf.entries();
          while (jarEntries.hasMoreElements()) {
              je = jarEntries.nextElement();
              String c=je.getName();
              if (c.endsWith(".class")) {
                  
                  CLS_FINDER.ok=true;
                  
                  ClassReader cr=new ClassReader(jf.getInputStream(je));
                  cr.accept(CLS_FINDER, 0);
                  ja.add(c, CLS_FINDER.ok);
              }
          }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

    }
    
    class ClassReferenceFinder implements ClassVisitor {
        
        public boolean ok=true;
        
        public void visit(int ver, int access, String name, String sig, String supr, String[] ifcs) {
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean arg1) {
            return null;
        }

        public void visitAttribute(Attribute arg0) {
        }

        public void visitEnd() {
        }

        public FieldVisitor visitField(int access, String name, String desc, String sig, Object value) {
            if((access & Opcodes.ACC_STATIC)!=0) {
                ok="serialVersionUID".equals(name) ? true:false;
            } 
            return null;
        }

        public void visitInnerClass(String name, String outer, String inner, int access) {
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] expts) {
            return null;
        }

        public void visitOuterClass(String owner, String name, String desc) {
        }

        public void visitSource(String arg0, String arg1) {
        }
    }

    public Analysis getAnalysis() {
        return analysis;
    }
}