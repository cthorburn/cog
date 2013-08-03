package com.trabajo.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.annotation.TaskState;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.process.IInstance;
import com.trabajo.utils.AnnotationUtils;

public class Lifecycle_END extends AbstractLifecycle {

	private Object processObject;

	public Lifecycle_END(LifecycleContext context, IInstance instance) {
		super(context);
		setInstance(instance);
		this.processObject = createProcessObject();
	}

	public void execute() throws ProcessException {
		Method m = getLifeCycleMethodByPhase(processObject.getClass(), com.trabajo.annotation.StandardLifecycle.COMPLETE);
		
		
		AnnotationHandlerChain before=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_INSTANCE_END, this);
		before.setContext(this);
		before.start();

		if(m!=null) {
			invokeLifeCycleMethod(processObject, m);
			saveState();
		}
		
		AnnotationHandlerChain after=AnnotationHandlerFactory.forPoint(ApplicationPoint.AFTER_INSTANCE_END, this);
		after.setContext(this);
		after.start();
	}

	private Field getStateContainerField(Object taskObj) {
		List<Field> fields = AnnotationUtils.getFieldsAnnotated(taskObj.getClass(), TaskState.class);
		return fields.isEmpty() ? null : fields.get(0);
	}

	private void saveState() {
		Field f = getStateContainerField(processObject);
		if(f!=null) {
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
	
			try {
				getInstance().setState((StateContainer) f.get(processObject));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		getInstance().save();
	}

	@Override
	public Object getTaskObject() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getProcessObject() {
		return processObject;
	}

	@Override
	public Object getTaskOrProcessObject() {
		return processObject;
	}
}