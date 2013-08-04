package com.trabajo.vcl;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.jar.JarFile;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

public final class ExtURLClassloader<T extends CLMKey<T>> extends URLClassLoader {

	private ClassLoaderManager<T, ?> clm;
	private Map<T, ExtURLClassloader<T>> overrideClassLoaders;
	private List<String> fixUpList;
	private T key;

	public ExtURLClassloader(ClassLoaderManager<T, ?> clm, final T key, final URL[] urls) throws RepositoryException {
		super(urls, new DummyURLClassLoader());
		this.clm = clm;
		this.key = key;
		overrideClassLoaders = new HashMap<>();
		fixUpList = clm.fixUpListFor(this);
	}

	public ExtURLClassloader(final T key, ExtURLClassloader<T> parent) {
		super(parent.getURLs(), parent);
		this.clm = parent.clm;
		this.key = key;
	}

	@Override
	public void close() throws IOException {
		if(overrideClassLoaders!=null) {
			for (ExtURLClassloader<T> orcl : overrideClassLoaders.values()) {
				orcl.close();
			}
		}
		clm = null;
		overrideClassLoaders = null;
		fixUpList = null;
		key = null;
		super.close();

        // now close any remaining streams.

        synchronized (closeables2) {
            Set<Closeable> keys = closeables2.keySet();
            for (Closeable c : keys) {
                   c.close();
            }
            closeables2.clear();
        }

	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {

		Class<?> c = null;
		final VCLThreadContext<T> pti = clm.getThreadContext();

		ClassLoaderChain<T> chain = clm.getProcessContextChain();

		ClassLoader parent;

		try {
			parent = chain.parent(this);
		} catch (PathNotFoundException e1) {
			throw new ClassNotFoundException(
					"The repository cannot find an entry for a parent classloader: "
							+ e1.getMessage());
		} catch (RepositoryException e1) {
			throw new ClassNotFoundException("repository exception loadclass: "
					+ name + " " + e1.getMessage());
		}

		if (parent == null) {
			parent = pti.containerClassLoader();
		}

		try {
			c = parent.loadClass(name);
		} catch (ClassNotFoundException e) {
			if (fixUpList != null && fixUpList.contains(name)) {
				final ExtURLClassloader<T> orcl = getKeySpecificClassLoader(pti
						.key());
				c = orcl.findLoadedClass(name);
				if (c == null) {
					c = orcl.findClass(name);
					if (resolve) {
						resolveClass(c);
					}
				}
			}

			else {
				c = findLoadedClass(name);
				if (c == null) {
					c = findClass(name);
					if (resolve) {
						resolveClass(c);
					}
				}
			}
		}

		return c;
	}

	@Override
	public URL findResource(final String name) {

		if (name == null) {
			return null;
		}

		final String normalizedName = name.startsWith("/") ? name.substring(1)
				: name;

		URL url = super.findResource(normalizedName);

		if (url == null) {
			final VCLThreadContext<T> pti = clm.getThreadContext();
			ClassLoaderChain<T> chain = clm.getProcessContextChain();
			ExtURLClassloader<T> parent;
			try {
				parent = chain.parent(this);
			} catch (RepositoryException e) {
				return null;
			}

			if (parent == null) {
				url = pti.containerClassLoader().getResource(name);
			} else {
				url = parent.findResource(name);
			}
		}

		return url;
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		final List<URL> urls = new ArrayList<URL>(3);

		final VCLThreadContext<T> pti = clm.getThreadContext();
		ClassLoaderChain<T> chain = clm.getProcessContextChain();

		for (T s : chain.reversed()) {
			ExtURLClassloader<T> search;
			try {
				search = chain.forName(s);
			} catch (RepositoryException e) {
				throw new IOException(e);
			}
			final URL url = search.superFindResource(name);
			if (url != null) {
				urls.add(url);
			}
		}

		final URL url = pti.containerClassLoader().getResource(name);

		if (url != null) {
			urls.add(url);
		}

		final Iterator<URL> itr = urls.iterator();

		return new Enumeration<URL>() {

			@Override
			public boolean hasMoreElements() {
				return itr.hasNext();
			}

			@Override
			public URL nextElement() {
				return itr.next();
			}
		};
	}

	private URL superFindResource(String name) {
		return super.findResource(name);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("ExtURLClassLoader finalised");
	}

	ExtURLClassloader<T> getKeySpecificClassLoader(T key) {
		ExtURLClassloader<T> orcl = overrideClassLoaders.get(key);

		if (orcl == null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(key.toPathString());
			sb.append("$");
			sb.append(overrideClassLoaders.size());

			orcl = new ExtURLClassloader<T>(key, this);
			synchronized (overrideClassLoaders) {
				overrideClassLoaders.put(key, orcl);
			}
		}

		return orcl;
	}

	public T key() {
		return key;
	}
	
	
	
    private WeakHashMap<Closeable,Void>
    closeables2 = new WeakHashMap<>();
	
	@SuppressWarnings("restriction")
	@Override
	public InputStream getResourceAsStream(String name) {
	        URL url = getResource(name);
	        try {
	            if (url == null) {
	                return null;
	            }
	            URLConnection urlc = url.openConnection();
	            InputStream is = urlc.getInputStream();
	            if (urlc instanceof JarURLConnection) {
	                JarURLConnection juc = (JarURLConnection)urlc;
	                JarFile jar = juc.getJarFile();
	                synchronized (closeables2) {
	                    if (!closeables2.containsKey(jar)) {
	                        closeables2.put(jar, null);
	                    }
	                }
	            } else if (urlc instanceof sun.net.www.protocol.file.FileURLConnection) {
	                synchronized (closeables2) {
	                    closeables2.put(is, null);
	                }
	            }
	            return is;
	        } catch (IOException e) {
	            return null;
	        }
	    }
}
