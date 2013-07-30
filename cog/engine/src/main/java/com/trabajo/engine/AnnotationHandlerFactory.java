package com.trabajo.engine;

import com.trabajo.engine.annotation.ApplicationPoint;

public class AnnotationHandlerFactory {
	private static final MetadataScanner scanner = new MetadataScanner();

	public static AnnotationHandlerChain forPoint(ApplicationPoint applicationPoint, AbstractLifecycle lifecycle) {
		AnnotationHandlerChain chain = new AnnotationHandlerChain();
		chain.set(scanner.handlersForPoint(applicationPoint), lifecycle);
		chain.init();
		return chain;
	}

}
