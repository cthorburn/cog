package com.trabajo.engine.annhandlers;

import com.trabajo.engine.AbstractLifecycle;
import com.trabajo.engine.annotation.Dependency;

public class AHDIEntityManager extends AHDI implements AnnotationHandlerDependencyInjector {
	@Override
	public void inject(AnnotationHandler ah, AbstractLifecycle l) {
		set(ah, Dependency.ENTITY_MANAGER.field(), l.getEntityManager());
	}
}
