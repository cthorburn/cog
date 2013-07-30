package com.trabajo.engine.annhandlers;

import com.trabajo.engine.AbstractLifecycle;
import com.trabajo.engine.annotation.Dependency;

public class AHDITaskOrProcessObject extends AHDI implements AnnotationHandlerDependencyInjector {
	@Override
	public void inject(AnnotationHandler ah, AbstractLifecycle l) {
		set(ah, Dependency.TASK_OR_PROCESS_OBJECT.field(), l.getTaskOrProcessObject());
	}
}
