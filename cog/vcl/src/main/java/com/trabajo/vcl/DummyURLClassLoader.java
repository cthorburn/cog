package com.trabajo.vcl;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

public class DummyURLClassLoader extends URLClassLoader {

	public DummyURLClassLoader() {
		super(new URL[0]);
	}

	@Override
    public final Class<?> findClass(String name) throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
	}

	@Override
	public final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
	}

	@Override
	public final URL findResource(String name) {
		return null;
	}

	@Override
	public final Enumeration<URL> findResources(String name) throws IOException {
		return new Enumeration<URL>() {

			@Override
			public boolean hasMoreElements() {
				return false;
			}

			@Override
			public URL nextElement() {
				return null;
			}
		};
	}
}
