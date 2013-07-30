package com.trabajo.annotation;

import java.net.URL;
import java.net.URLClassLoader;

public class AnnotationQueryClassLoader extends URLClassLoader {

	public AnnotationQueryClassLoader() {
		super(new URL[0], Thread.currentThread().getContextClassLoader());
	}

	public Class<?> define(String mainClassName, byte[] b) {
		return defineClass(mainClassName, b, 0, b.length);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
