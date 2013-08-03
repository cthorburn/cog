package com.trabajo.tomcat;

import java.io.File;
import java.util.Hashtable;

import org.apache.naming.resources.FileDirContext;

public class TrabajoDirContext extends FileDirContext {

	public TrabajoDirContext() {
		super();
		System.out.println("*************************** TrabajoDirContext is active!");
	}

	@SuppressWarnings("rawtypes")
	public TrabajoDirContext(Hashtable env) {
		super(env);
		System.out.println("*************************** TrabajoDirContext is active!");
	}

	@Override
	protected File file(String path) {
		if(TomcatCLMProxy.instance==null)
			return super.file(path);;
		
			if(path.endsWith("my.jsp")) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>my.jsp");
			}
		File f=TomcatCLMProxy.instance.findFile(path);
		return (f != null && f.exists()) ? f : super.file(path);
	}
}
