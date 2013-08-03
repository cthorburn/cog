package com.trabajo.engine;

import com.trabajo.DefinitionVersion;
import com.trabajo.vcl.ClassLoaderManager;

public class EngineFactory {

	
	private static Engine engine;
	private static FileSystem fs;
	private static JackRabbitProcessStore store;
	private static FileCache fc;
	private static ProcessRegistry pr;
	private static ClassLoaderManager<DefinitionVersion, DVFactory> clm;
	private static SandBoxFactory sbf;
	
	public static void init(SysConfig config){
		fs=new FileSystem(config);
		store=new JackRabbitProcessStore(config.getJackRabbitURL(), fs);
		fc=new FileCacheImpl(store);
		pr=new ProcessRegistry(store, fc);
		clm=new ClassLoaderManager<>(pr, new DVFactory()); 
		sbf=new SandBoxFactory(clm);
		engine=new EngineImpl(pr, sbf);
	}
	
	
	
	
	public static Engine getEngine() {
		return engine;
	}
	
	public static FileCache fileCache() {
		return fc;
	}
}
