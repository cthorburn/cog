package com.trabajo.engine.annhandlers;

import com.trabajo.engine.AbstractLifecycle;

public interface AnnotationHandlerDependencyInjector {
	void inject(AnnotationHandler ah, AbstractLifecycle l);
}
