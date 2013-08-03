package com.trabajo.engine;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import javax.jcr.RepositoryException;

import com.trabajo.DefinitionVersion;

public interface FileCache extends Observer {

    /**
     * Turn repository classloader URLS into classloader useful ones
     */
    URL[] resolve(String[] repositoryClassLoaderURLS);
    URL[] resolve(URL[] repositoryClassLoaderURLS);
    
    void load(DefinitionVersion dv) throws RepositoryException;

    File classLoaderURLToFile(String url);
    File classLoaderURLToFile(URL url);
    File cacheURLToFile(URL url);
    File[] classLoaderURLsToFiles(URL[] classLoaderURLS);
    File[] classLoaderURLsToFiles(String[] classLoaderURLS);
    File[] cacheURLsToFiles(URL[] cacheURLs);
    
	File findFile(URL[] urls, String muggledPath);
	List<String> unzipDirNames(DefinitionVersion dv);
	Map<String, String> unzipDirPaths(DefinitionVersion dv);
	File baseDir(DefinitionVersion dv);
	File getFile(DefinitionVersion dv, String path);
	void addObserver(Observer o);
}
