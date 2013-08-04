package com.trabajo.vcl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaderChains<T extends CLMKey<T>> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ClassLoaderChains.class);

	private Map<T, ClassLoaderChain<T>> chains;
	private Map<T, ExtURLClassloader<T>> namedClassLoaders;
	private ClassLoaderManager<T, ?> classLoaderManager;

	public ClassLoaderChains(ClassLoaderManager<T, ?> classLoaderManager,
			ClassLoaderChainProvider<T> pr) {
		this.classLoaderManager = classLoaderManager;
		chains = new HashMap<>();
		namedClassLoaders = new HashMap<>();
	}

	public synchronized void putOutOfService(T key) {
		System.out.print("put out of service: ");
		ClassLoaderChain<T> chain = chains.remove(key);
		if (chain != null) {
			System.out.print("chain exists: "+key.toString());
		}
		evict(key);
	}

	public synchronized void evict(T key) {
		ExtURLClassloader<T> cl=this.namedClassLoaders.remove(key);
		
		if (cl != null) {
			try {
				cl.close();
				System.out.print(" closed OK ");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public final ClassLoaderChain<T> loadChain(T t, List<T> classLoaderNames) {
		ClassLoaderChain<T> chain = chains.get(t);
		if (chain == null) {

			chain = new ClassLoaderChain<T>(t, this, classLoaderNames);
			synchronized (chains) {
				chains.put(t, chain);
			}
		}
		return chain;
	}

	public final ClassLoaderChain<T> getChain(T dv) {
		return chains.get(dv);
	}

	public final ExtURLClassloader<T> getNamedClassLoader(T name)
			throws RepositoryException {
		ExtURLClassloader<T> extCl = namedClassLoaders.get(name);
		if (extCl == null) {
			extCl = classLoaderManager.newExtURLClassLoader(name);
			synchronized (namedClassLoaders) {
				namedClassLoaders.put(name, extCl);
			}
		}
		return extCl;
	}

}
