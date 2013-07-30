package com.trabajo.engine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.IVisualizer;
import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.annotation.StandardTaskLifecycle;
import com.trabajo.annotation.TaskLifecycle;
import com.trabajo.annotation.TaskState;
import com.trabajo.annotation.Visualizer;
import com.trabajo.process.ITask;
import com.trabajo.utils.AnnotationUtils;

public class TaskPhaseRunner {
	private final static Logger logger = LoggerFactory.getLogger(TaskPhaseRunner.class);

	private Object taskObj;
	private StandardTaskLifecycle phase;
	private TSession ts;
	private Object parms;
	private ITask task;

	public TaskPhaseRunner(Object taskObj, StandardTaskLifecycle phase, TSession ts, Object parms, ITask task) {
		this.taskObj = taskObj;
		this.phase = phase;
		this.ts = ts;
		this.parms = parms;
		this.task=task;
	}
	
	public void run() throws ProcessException {
		run(null);
	}

	public void run(String action) throws ProcessException {

		EngineStatus status = ts.getStatus();

		Method m = getMethodPhaseMethod(action);
		if (m == null) {
			return;
		}

		try {
			if(m.getParameterTypes().length==0) {
				m.invoke(taskObj);
			} else {
				m.invoke(taskObj, parms);
			}
			Field f=getStateContainerField(taskObj);
			if(f!=null) {
				if(!f.isAccessible()) {
					f.setAccessible(true);
				}
				task.setStateContainer((StateContainer)f.get(taskObj));
			}
			
			f=getVizualizerField(taskObj);
			if(f!=null) {
				if(!f.isAccessible()) {
					f.setAccessible(true);
				}
				task.getInstance().setVisualizer((IVisualizer)f.get(taskObj));
			}
			
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

	private Field getVizualizerField(Object taskObj) {
		List<Field> fields= AnnotationUtils.getFieldsAnnotated(taskObj.getClass(), Visualizer.class);
		return fields.isEmpty() ? null: fields.get(0);
	}

	private Method getMethodPhaseMethod(String action) {
		for (Method m : taskObj.getClass().getMethods()) {
			if (m.isAnnotationPresent(TaskLifecycle.class)) {
				TaskLifecycle l = m.getAnnotation(TaskLifecycle.class);
				if (l != null && l.phase() == phase) {
					
					if(action!=null && l.action().equals(action)) {
						return m;
					}
					else if(action==null) {
						return m;
					}
				}
			}
		}
		return null;
	}
}
