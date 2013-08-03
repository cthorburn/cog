package com.trabajo.vcl;

import java.util.List;

public final class VCLThreadContext<T extends CLMKey<T>> {
	private final ClassLoader containerClassLoader;
	private final T key;
	private List<T> classLoaderNames;
	private ClassLoaderChain<T> chain;

	public ClassLoaderChain<T> getChain() {
		return chain;
	}

	public void setChain(ClassLoaderChain<T> chain) {
		this.chain = chain;
	}

	public VCLThreadContext(final ClassLoader containerClassLoader, T key, List<T> classLoaderNames) {
		if (containerClassLoader==null || containerClassLoader instanceof ExtURLClassloader) {
			throw new RuntimeException("Invalid VCLThreadContext containerClassLoader type");
		}
		this.containerClassLoader = containerClassLoader;
		this.key = key;
		this.classLoaderNames = classLoaderNames;
	}

	public ClassLoader containerClassLoader() {
		return containerClassLoader;
	}

	public void revertClassLoader() {
		Thread.currentThread().setContextClassLoader(containerClassLoader);
	}

	public T key() {
		return key;
	}

	public List<T> classLoaderNames() {
		return this.classLoaderNames;
	}
}
