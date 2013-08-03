package com.trabajo.engine.annotation;

import com.trabajo.engine.annhandlers.AHDIClassLoaderManager;
import com.trabajo.engine.annhandlers.AHDIEntityManager;
import com.trabajo.engine.annhandlers.AHDILifecycle;
import com.trabajo.engine.annhandlers.AHDIProcessObject;
import com.trabajo.engine.annhandlers.AHDIProcessRegistry;
import com.trabajo.engine.annhandlers.AHDIProcessVersion;
import com.trabajo.engine.annhandlers.AHDITaskObject;
import com.trabajo.engine.annhandlers.AHDITaskOrProcessObject;
import com.trabajo.engine.annhandlers.AnnotationHandlerDependencyInjector;

public enum Dependency {
	ENTITY_MANAGER(new AHDIEntityManager(), "___em"),
	LIFECYCLE(new AHDILifecycle(), "___lifecycle"), 
	PROCESS_OBJECT(new AHDIProcessObject(), "___processObject"), 
	TASK_OBJECT(new AHDITaskObject(), "___taskObject"),
	TASK_OR_PROCESS_OBJECT(new AHDITaskOrProcessObject(), "___taskOrProcessObject"),
	CLASSLOADER_MANAGER(new AHDIClassLoaderManager(), "___classLoaderManager"),
	PROCESS_VERSION(new AHDIProcessVersion(), "___processVersion"),
	PROCESS_REGISTRY(new AHDIProcessRegistry(), "___processRegistry");
	
	private AnnotationHandlerDependencyInjector injector;
	private String field;
	
	private Dependency(AnnotationHandlerDependencyInjector injector, String field) {
		this.injector=injector;
		this.field=field;
	}

	public AnnotationHandlerDependencyInjector injector() {
		return injector;
	}

	public String field() {
		return field;
	}
}
