package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

import com.trabajo.ILifecycle;
import com.trabajo.annotation.ATask;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.process.ITask;
import com.trabajo.utils.FieldVisitor;
import com.trabajo.utils.Visitation;

@Component(
		applicationPoints = { 
			ApplicationPoint.BEFORE_TASK_CREATE, 
			ApplicationPoint.BEFORE_TASK_DATA, 
			ApplicationPoint.BEFORE_TASK_TIMEOUT,
			ApplicationPoint.BEFORE_TASK_VIEW,
			ApplicationPoint.BEFORE_TASK_DISPOSE }, 
		dependencies = { 
				Dependency.LIFECYCLE, 
				Dependency.TASK_OBJECT })


public class TaskAnnotationHandler implements AnnotationHandler {

	private Object ___taskObject;
	private ILifecycle ___lifecycle;

	@Override
	public void process() {
		process(___taskObject, ___lifecycle);
	}

	private void process(final Object taskObject, final ILifecycle l) {

		new FieldVisitor(taskObject, new Visitation<Field>() {
			@Override
			public void visit(Field it) {
				ATask ann = it.getAnnotation(com.trabajo.annotation.ATask.class);
				if (ann != null) {
					ITask inject = l.getTask();
					inject.setLifecycle(l);

					if (!it.isAccessible()) {
						it.setAccessible(true);
					}
					try {
						it.set(taskObject, inject);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
	}
}
