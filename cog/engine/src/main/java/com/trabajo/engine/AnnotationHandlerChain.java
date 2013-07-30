package com.trabajo.engine;

import java.util.List;

import com.trabajo.engine.annhandlers.AnnotationHandler;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;

class AnnotationHandlerChain {

	private AbstractLifecycle lifecycle;
	private List<AnnotationHandler> handlers;
	
	void start() {
		for(AnnotationHandler ah: handlers) {
			ah.process();
		}
	}

	void setContext(final AbstractLifecycle lifecycle) {
		this.lifecycle=lifecycle;
	}

	void init() {
		for(AnnotationHandler ah: handlers) {
			init(ah);
		} 
	}

	private void init(AnnotationHandler ah) {
		injectDependencies(ah);
	}

	private void injectDependencies(AnnotationHandler ah) {
		Component ann=ah.getClass().getAnnotation(Component.class);
		Dependency[] deps=ann.dependencies();
		
		for(Dependency dep: deps) {
			dep.injector().inject(ah, lifecycle);
		}
	}

	void set(List<AnnotationHandler> handlers, AbstractLifecycle lifecycle) {
		this.handlers=handlers;
		this.lifecycle=lifecycle;
	}
}
