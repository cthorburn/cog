package com.trabajo.tomcat;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.catalina.loader.ResourceEntry;


public class CogBaseClassLoader extends org.apache.tomee.catalina.LazyStopWebappClassLoader {

	@Override
	public URL getResource(String arg0) {
	if(arg0.endsWith(".jsp"))	
			System.out.println("getResource: "+arg0);
		return super.getResource(arg0);
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		if(arg0.endsWith(".jsp"))	
			System.out.println("getResourceAsStream: "+arg0);
		return super.getResourceAsStream(arg0);
	}

	@Override
	public URL findResource(String arg0) {
		if(arg0.endsWith(".jsp"))	
			System.out.println("findResource: "+arg0);
		return super.findResource(arg0);
	}

	@Override
	protected ResourceEntry findResourceInternal(File arg0, String arg1) {
		if(arg0.getAbsolutePath().endsWith(".jsp"))	
			System.out.println("findResourceInternal: "+arg0);
		if(arg1.endsWith(".jsp"))	
			System.out.println("findResourceInternal: "+arg0);
		return super.findResourceInternal(arg0, arg1);
	}

	@Override
	protected ResourceEntry findResourceInternal(String arg0, String arg1) {
		if(arg0.endsWith(".jsp"))	
			System.out.println("findResourceInternal: "+arg0);
		if(arg1.endsWith(".jsp"))	
			System.out.println("findResourceInternal: "+arg0);
		return super.findResourceInternal(arg0, arg1);
	}

	public CogBaseClassLoader() {
		super();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>cogCL created: ");
	}

	public CogBaseClassLoader(ClassLoader parent) {
		super(parent);
		if(parent!=null)
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>cogCL created: "+parent.getClass().getName());
		else
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>cogCL created: null parent");
	}
}
