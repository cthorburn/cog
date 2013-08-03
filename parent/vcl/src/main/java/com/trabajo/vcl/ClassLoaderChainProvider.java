package com.trabajo.vcl;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Observer;

import javax.jcr.RepositoryException;

public interface ClassLoaderChainProvider<T extends CLMKey<T>> {

	URL[] jarURLsForVersion(T key) throws RepositoryException;


    List<String> fixupListFor(T key) throws RepositoryException;

	/**
	 * Find the given (non-magical/muggled) file within the resources of the classLoaders
	 * specified by the names param
	 *
	 * @param names
	 * @param muggle
	 * @return
	 */
	File findFile(T clmKey, String muggle) throws RepositoryException;

	T getContextKey();
	
	void addObserver(Observer o);
}
