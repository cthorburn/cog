package com.trabajo.engine.annhandlers;

import com.trabajo.engine.AbstractLifecycle;
import com.trabajo.engine.annotation.Dependency;

public class AHDIProcessRegistry extends AHDI implements AnnotationHandlerDependencyInjector {
	@Override
	public void inject(AnnotationHandler ah, AbstractLifecycle l) {
		set(ah, Dependency.PROCESS_REGISTRY.field(), l.getProcessRegistry());
	}
}
