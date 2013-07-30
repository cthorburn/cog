package com.trabajo.engine.annhandlers;

import com.trabajo.engine.AbstractLifecycle;
import com.trabajo.engine.annotation.Dependency;

public class AHDITaskObject extends AHDI implements AnnotationHandlerDependencyInjector {
	@Override
	public void inject(AnnotationHandler ah, AbstractLifecycle l) {
		try{
		set(ah, Dependency.TASK_OBJECT.field(), l.getTaskObject());
		}catch(UnsupportedOperationException e){
			//ignore
		}
	}
}
