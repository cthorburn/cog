package com.trabajo.engine;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.WholeJarMetadataV1;
import com.trabajo.vcl.ClassLoaderManager;
import com.trabajo.vcl.ExtURLClassloader;

public class LifecycleContext {

	private TSession ts; 
	private ExtURLClassloader<DefinitionVersion> classLoader;
	private ClassLoaderManager<DefinitionVersion, DVFactory> clm;
	private ProcessClassMetadata pcm; 
	private WholeJarMetadataV1 wjm;
	
	public LifecycleContext(
			TSession ts, 
			ExtURLClassloader<DefinitionVersion> firstClassLoader, 
			ClassLoaderManager<DefinitionVersion, DVFactory> clm,
			ProcessClassMetadata pcm, 
			WholeJarMetadataV1 wjm) {
		super();
		this.ts = ts;
		this.classLoader = firstClassLoader;
		this.clm = clm;
		this.pcm = pcm;
		this.wjm = wjm;
	}

	public TSession session() {
		return ts;
	}

	public String getRemoteUser() {
		return ts.getRemoteUser();
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public WholeJarMetadataV1 getWjm() {
		return wjm;
	}

	public ProcessClassMetadata getPcm() {
		return pcm;
	}

	public ClassLoaderManager<DefinitionVersion, DVFactory> getClassLoaderManager() {
		return clm;
	}

	public ProcessRegistry getProcessRegistry() {
		return (ProcessRegistry)clm.getChainProvider();
	}
}
