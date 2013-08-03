package com.trabajo.vcl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.jcr.RepositoryException;

public class ClassLoaderChain<T extends CLMKey<T>> {

    private final List<T> names;
    private final ClassLoaderChains<T> chains;
    private final List<T> reversed;
    private static final int SIX_WTF = 6; // WTF ???
    private T clmKey;

    public ClassLoaderChain(T clmKey, final ClassLoaderChains<T> chains, final List<T> names) {
    	 this.clmKey=clmKey;
        this.chains = chains;
        this.names = Collections.unmodifiableList(names);
        final List<T> tmp = new ArrayList<>();
        tmp.addAll(names);
        Collections.reverse(tmp);
        reversed = Collections.unmodifiableList(tmp);
    }

    public T getClmKey() {
			return clmKey;
		}

		public final List<T> names() {
        return names;
    }

    public final List<T> reversed() {
        return reversed;
    }

    public final ExtURLClassloader<T> parent(ExtURLClassloader<T> extURLClassloader) throws RepositoryException {

        ExtURLClassloader<T> parent = null;
        T parentName = null;

        try {
            parentName = names.get(names.indexOf(extURLClassloader.key()) + 1);
        } catch (IndexOutOfBoundsException e) {
            // extURLClassloader is top of chain
            return null;
        }

        parent = chains.getNamedClassLoader(parentName);

        return parent;
    }

    public final ExtURLClassloader<T> forName(T name) throws RepositoryException {
        return chains.getNamedClassLoader(name);
    }

    public final File findFile(String path) throws RepositoryException {
        return urlToFile(findResource(path));
    }

    private File urlToFile(URL url) {
        return url == null ? null : new File(url.toString().substring(SIX_WTF));
    }

    public final URL findResource(String name) throws RepositoryException {
        return chains.getNamedClassLoader(names.get(0)).findResource(name);
    }

    public final Enumeration<URL> findResources(String name) throws IOException, RepositoryException {
        return chains.getNamedClassLoader(names.get(0)).findResources(name);
    }

    public final InputStream getResourceAsStream(String name) throws RepositoryException {
        return chains.getNamedClassLoader(names.get(0)).getResourceAsStream(name);
    }

    public final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException, RepositoryException {
            return getFirst().loadClass(name, resolve);
    }

    public final ExtURLClassloader<T> getFirst() throws RepositoryException  {
        return chains.getNamedClassLoader(names.get(0));
    }
}
