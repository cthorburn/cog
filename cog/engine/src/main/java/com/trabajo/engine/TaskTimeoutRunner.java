package com.trabajo.engine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.TimerType;
import com.trabajo.annotation.TaskState;
import com.trabajo.annotation.Timeout;
import com.trabajo.process.ITask;
import com.trabajo.utils.AnnotationUtils;

public class TaskTimeoutRunner {
	private final static Logger logger = LoggerFactory.getLogger(TaskTimeoutRunner.class);

	private Object taskObj;
	private TSession ts;
	private ITask task;
	private TimerType type;
	
	public TaskTimeoutRunner(Object taskObj, TSession ts, ITask task, TimerType type) {
		this.taskObj = taskObj;
		this.ts = ts;
		this.task=task;
		this.type= type;
	}
	
	public void run() throws ProcessException {

		EngineStatus status = ts.getStatus();

		Method m = getTimeoutMethod();
		if (m == null) {
			return;
		}

		try {
			m.invoke(taskObj);
			
			Field f=getStateContainerField(taskObj);
			if(!f.isAccessible()) {
				f.setAccessible(true);
			}
			
			task.setStateContainer((StateContainer)f.get(taskObj));
	
			
		} catch (InvocationTargetException e) {
			status.error(e.getTargetException().getMessage(), logger, (Exception) e.getTargetException());
		} catch (IllegalAccessException | IllegalArgumentException e) {
			status.error("Failed to invoke lifecycle method: " + m.getName() + ": " + e.getMessage(), logger, e);
		} catch (RuntimeException e) {
			status.error("The process threw an unexpected exception: " + m.getName() + ": " + e.getMessage(), logger, e);
		}
	}

	private Field getStateContainerField(Object taskObj) {
		List<Field> fields= AnnotationUtils.getFieldsAnnotated(taskObj.getClass(), TaskState.class);
		return fields.isEmpty() ? null: fields.get(0);
	}

	private Method getTimeoutMethod() {
		for (Method m : taskObj.getClass().getMethods()) {
			if (m.isAnnotationPresent(Timeout.class)) {
				Timeout l = m.getAnnotation(Timeout.class);
				if (l.type()==this.type) {
						return m;
				}
			}
		}
		return null;
	}
}
