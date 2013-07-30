package com.trabajo.vcl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaderChains<T extends CLMKey<T>> implements Observer {

	private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderChains.class);

	private Map<T, ClassLoaderChain<T>> chains;
	private Map<T, ExtURLClassloader<T>> namedClassLoaders;
	private ClassLoaderManager<T, ?> classLoaderManager;

	public ClassLoaderChains(ClassLoaderManager<T, ?> classLoaderManager, ClassLoaderChainProvider<T> pr) {
		this.classLoaderManager = classLoaderManager;
		chains = new HashMap<>();
		namedClassLoaders = new HashMap<>();
		pr.addObserver(this);
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

	public final ExtURLClassloader<T> getNamedClassLoader(T name) throws RepositoryException {
		ExtURLClassloader<T> extCl = namedClassLoaders.get(name);
		if (extCl == null) {
			extCl = classLoaderManager.newExtURLClassLoader(name);
			synchronized (namedClassLoaders) {
				namedClassLoaders.put(name, extCl);
			}
		}
		return extCl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
	}
	
//		ChainUpdate<T> chu;
//		try {
//			chu = (ChainUpdate<T>) arg;
//		} catch (ClassCastException e) {
//			return;
//		}
//		CLMKey<T> dv = chu.getClassLoaderVersion();
//
//		if (chu.type() == ChainUpdate.TYPE.REDEFINE || chu.type() == ChainUpdate.TYPE.DELETE) {
//			synchronized (namedClassLoaders) {
//				ExtURLClassloader<T> cl = namedClassLoaders.remove(dv.toString());
//				if (cl != null) {
//					try {
//						cl.close();
//						if (chu.type() == ChainUpdate.TYPE.DELETE) {
//							try {
//								chu.deleteFiles();
//							} catch (IOException e) {
//								LOG.error(
//										"DELETE CLASSLOADER: ClassLoader was closed OK but unable to delete a, some or all classloader jars from local cache for : "
//												+ cl.getLogicalName(), e);
//								logListJars(chu);
//								throw new RuntimeException(e);
//							}
//						}
//					} catch (Exception e) {
//						LOG.error("DELETE/REDEFINE CLASSLOADER: Unable to close classloader: " + cl.getLogicalName(), e);
//						LOG.error("DELETE/REDEFINE CLASSLOADER: Unable to delete a, some, or all classloader jars from local cache for : " + cl.getLogicalName(), e);
//						logListJars(chu);
//						throw new RuntimeException(e);
//					}
//				}
//			}
//		}
//	}

	private void logListJars(ChainUpdate<T> chu) {
		File[] jars = chu.getFiles();

		LOG.error("The following may need to be deleted by stopping the container ------------------------- ");
		for (File f : jars) {
			LOG.error(f.getAbsolutePath());
		}
		LOG.error("----------------------------------------------------------------------------------------- ");
	}

	public void obliterate(T key) {
		ExtURLClassloader<T> cl;
		synchronized (namedClassLoaders) {
			cl = namedClassLoaders.remove(key.toString());
			namedClassLoaders.put(key, null);
		}
		if (cl != null) {
			try {
				cl.close();
			} catch (IOException e) {
				// ignoreTODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
