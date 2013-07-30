package com.trabajo.tomcat;

import org.apache.catalina.Context;


public class CogBaseLoader { //extends LazyStopWebappLoader {

	public CogBaseLoader() {
		super();
		System.out.println("CogbaseLoader======================================================");
	}

	public CogBaseLoader(Context ctx) {
		//super(ctx);
		System.out.println("CogbaseLoader======================================================");
	}
	
}
