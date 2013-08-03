package com.trabajo.vcl;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.jcr.RepositoryException;

import com.trabajo.tomcat.DirContextFileFinder;
import com.trabajo.tomcat.TomcatCLMProxy;

public final class ClassLoaderManager<T extends CLMKey<T>, F extends KeyFactory<T>> extends Observable implements Observer, DirContextFileFinder {

	private final ThreadLocal<VCLThreadContext<T>> threadContext;
	private ClassLoaderChains<T> chains;
	private ClassLoaderChainProvider<T> pr;
	private PathMagic<T> pathMagic;
	private F tFactory;

	public ClassLoaderManager(ClassLoaderChainProvider<T> pr, F tFactory) {
		this.pr = pr;
		this.pathMagic = new PathMagic<T>(this);
		this.tFactory = tFactory;
		chains = new ClassLoaderChains<T>(this, pr);
		threadContext = new ThreadLocal<VCLThreadContext<T>>();

		try {
			TomcatCLMProxy.instance = this;
		} catch (Exception e) {

		}
		
		pr.addObserver(this);
	}

	public ClassLoaderChainProvider<T> getChainProvider() {
		return pr;
	}
	
	public void clearProcessContext() {
		final VCLThreadContext<T> pti = threadContext.get();
		if (pti != null) {
			pti.revertClassLoader();
			threadContext.set(null);
		}
	}

	public VCLThreadContext<T> setProcessContext(T t, List<T> classLoaderNames) throws NoSuchResourceException, ResourceUnavailableException, RepositoryException {

		VCLThreadContext<T> pti = threadContext.get();

		if (pti == null || !pti.key().equals(t)) {
			clearProcessContext();
			final Thread currentThread = Thread.currentThread();

			pti = new VCLThreadContext<>(currentThread.getContextClassLoader(), t, classLoaderNames);
			threadContext.set(pti);

			final ClassLoaderChain<T> chain = getProcessContextChain();
			final ExtURLClassloader<T> pcl = chain.getFirst();
			pti.setChain(chain);
			currentThread.setContextClassLoader(pcl);
		}

		return pti;
	}

	public ClassLoaderChain<T> getProcessContextChain() {

		final VCLThreadContext<T> pti = threadContext.get();
		if (pti == null) {
			return null;
		}

		ClassLoaderChain<T> chain = chains.getChain(pti.key());

		if (chain == null) {
			chain = chains.loadChain(pti.key(), pti.classLoaderNames());
		}

		return chain;
	}

	public ExtURLClassloader<T> newExtURLClassLoader(T key) throws RepositoryException {
		return new ExtURLClassloader<T>(this, key, pr.jarURLsForVersion(key));
	}

	public File findFile(String path) {

		ClassLoaderChain<T> chain = null;
		File f = null;

		final Magician<T> k = pathMagic.magicForPath(path);

		if (k == null) {
			chain = getProcessContextChain();
			if (chain != null) {
				try {
					f = pr.findFile(chain.getClmKey(), path);
				} catch (RepositoryException e) {
					return null;
				}
			}
		} else {
			T dv = tFactory.parse(k.extractCLMKeyFactoryString(path));

			try {
				f = pr.findFile(dv, k.muggle(path));
			} catch (RepositoryException e) {
				return null;
			}
		}
		return f;
	}

	VCLThreadContext<T> getThreadContext() {
		return threadContext.get();
	}

	List<String> fixUpListFor(ExtURLClassloader<T> extURLCl) throws RepositoryException {
		return pr.fixupListFor(extURLCl.key());
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException, RepositoryException {
		return getThreadContext().getChain().loadClass(name, true);
	}

	public Magician<T> getResourceMagician() {
		return pathMagic.getResourceMagician();
	}
	


	@Override
	public void update(Observable o, Object arg) {
		try {
			@SuppressWarnings("unchecked") 
			ChainUpdate<T> chu = (ChainUpdate<T>) arg;

			T key= chu.getClassLoaderVersion();

			if (chu.type() == ChainUpdate.TYPE.DELETE) {
				chains.putOutOfService(key);
			}
			
			setChanged();
			notifyObservers(new ChainUpdate<T>(ChainUpdate.TYPE.CHAIN_OUT_OF_SERVICE, key));

		} catch (ClassCastException e) {
			return;
		}
	}

	public void clFinalized() {
		// TODO Auto-generated method stub
		
	}
}
