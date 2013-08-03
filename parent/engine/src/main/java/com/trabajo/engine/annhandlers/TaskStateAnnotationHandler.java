package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

import com.trabajo.ILifecycle;
import com.trabajo.annotation.TaskState;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.process.ITask;
import com.trabajo.utils.FieldVisitor;
import com.trabajo.utils.Visitation;

@Component(applicationPoints = {
		ApplicationPoint.BEFORE_INSTANCE_START,
		ApplicationPoint.BEFORE_TASK_CREATE, 
		ApplicationPoint.BEFORE_TASK_DATA, 
		ApplicationPoint.BEFORE_TASK_TIMEOUT,
		ApplicationPoint.BEFORE_TASK_VIEW,
		ApplicationPoint.BEFORE_TASK_DISPOSE }, 
		dependencies = { 
		Dependency.LIFECYCLE, 
		Dependency.TASK_OBJECT })

public class TaskStateAnnotationHandler implements AnnotationHandler {

	private Object ___taskObject;
	private ILifecycle ___lifecycle;

	@Override
	public void process() {
		process(___taskObject, ___lifecycle);
	}

	private void process(final Object o, final ILifecycle l) {

		final ITask task;

		try {
			task = l.getTask();
		} catch (UnsupportedOperationException e) {
			return;
		}

		new FieldVisitor(o, new Visitation<Field>() {
			@Override
			public void visit(Field it) {
				TaskState ann = it.getAnnotation(TaskState.class);
				if (ann != null) {
					try {
						if (!it.isAccessible()) {
							it.setAccessible(true);
						}

						it.set(o, task.getState());
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});

	}
}
