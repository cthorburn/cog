package com.trabajo.vcl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observer;

import javax.jcr.RepositoryException;

import org.junit.Test;

public class VPUClassLoaderTest {

    public static final class Key implements CLMKey<Key> {
        private String value;

        public Key(String value) {
            this.value = value;
        }

        @Override
        public String toPathString() {
            return value;
        }

				@Override
				public int compareTo(Key o) {
					return value.compareTo(o.toPathString());
				}
    }

    public static final class KeyFact implements KeyFactory<Key> {
        @Override
        public Key parse(String s) {
            return new Key(s);
        }
    }

    public static final class Provider implements ClassLoaderChainProvider<Key> {

        private static final String BASE = "file:///C:\\_GITHUB\\giteval\\";

        private static final ThreadLocal<Key> KEY = new ThreadLocal<Key>() {
            @Override
            protected Key initialValue() {
                return new Key(String.valueOf(System.currentTimeMillis()));
            }
        };

        @Override
        public URL[] jarURLsForVersion(Key key) {
            switch (key.toPathString()) {
            case "CL1":
                return new URL[] { uFor("p_a_v1") };
            case "CL2":
                return new URL[] { uFor("p_b_v1") };
            case "CL3":
                return new URL[] { uFor("p_a_v2") };
            case "CL4":
                return new URL[] { uFor("p_c_v1") };
            case "CL5":
                return new URL[] { uFor("p_c_v2") };
            case "CL6":
                return new URL[] { uFor("common") };
            default:
                return null;
            }
        }

        private URL uFor(String jarName) {
            try {
                return new URL(BASE + jarName + "\\target\\" + jarName + "-0.0.1-SNAPSHOT.jar");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        public List<String> fixupListFor(Key key) {
            switch (key.value) {
            case "CL1":
                return Collections.emptyList();
            case "CL2":
                return new ArrayList<String>() { private static final long serialVersionUID = 1L;  { add("p.b.B"); } };
            case "CL3":
                return Collections.emptyList();
            case "CL4":
                return Collections.emptyList();
            case "CL5":
                return Collections.emptyList();
            case "CL6":
                return Collections.emptyList();
            default:
                return null;
            }
        }

        @Override
        public File findFile(Key dv, String muggle) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Key getContextKey() {
            return KEY.get();
        }

				@Override
				public void addObserver(Observer o) {
					// TODO Auto-generated method stub
					
				}

    }

    public VPUClassLoaderTest() {

    }

    @SuppressWarnings("unchecked")
    @Test
    public final void shouldReplaceClassWithInconsistentClassLoaderReferences() throws Exception {

        final Provider pr = new Provider();
        final ClassLoaderManager<Key, KeyFact> clm = new ClassLoaderManager<Key, KeyFact>(pr, new KeyFact());

        final Thread a = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    clm.setProcessContext(pr.getContextKey(), Arrays.asList(new Key("CL1"), new Key("CL2"), new Key("CL4"), new Key("CL6")));
                    final ExtURLClassloader<Key> newContextCl = ((ExtURLClassloader<Key>) Thread.currentThread().getContextClassLoader());
                    newContextCl.loadClass("p.a.A").newInstance();
                    Class.forName("p.a.A", true, newContextCl);
                } catch (NoSuchResourceException | ResourceUnavailableException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (RepositoryException e) {
                  throw new RuntimeException(e);
								}
            }

        });
        a.start();

        System.out.println("------------------------------------------");

        final Thread b = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    clm.setProcessContext(pr.getContextKey(), Arrays.asList(new Key("CL3"), new Key("CL2"), new Key("CL5"), new Key("CL6")));
                    final ExtURLClassloader<Key> newContextCl = ((ExtURLClassloader<Key>) Thread.currentThread().getContextClassLoader());
                    newContextCl.loadClass("p.a.A").newInstance();
                    Class.forName("p.a.A", true, newContextCl);
                } catch (NoSuchResourceException | ResourceUnavailableException | InstantiationException | IllegalAccessException | ClassNotFoundException | RepositoryException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        b.start();

        a.join();
        b.join();
    }
}
