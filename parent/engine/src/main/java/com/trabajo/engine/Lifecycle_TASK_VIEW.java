package com.trabajo.engine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.TaskCompletion;
import com.trabajo.annotation.StandardTaskLifecycle;
import com.trabajo.annotation.TaskLifecycle;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.process.ITask;
import com.trabajo.process.ITaskGroup;

public class Lifecycle_TASK_VIEW extends AbstractLifecycle {
	
	@Override
	public void setView(View view) {
		status.setResult(view);
	}

	private final static Logger logger = LoggerFactory.getLogger(SandBoxImpl.class);

	private Object taskObject;
	private ITask node;
	
	public Lifecycle_TASK_VIEW(LifecycleContext context, ITask node) {
		super(context);
		setInstance(node.getInstance());
		this.node=node;
		taskObject= createTaskObject(node);
		AnnotationHandlerChain before=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_TASK_VIEW, this);
		before.setContext(this);
		before.start();
	}

	public void dispose(View view, HttpServletRequest request, HttpServletResponse response) {
		try {
			view.dispose(session(), request, response);
			AnnotationHandlerChain after=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_TASK_VIEW, this);
			after.setContext(this);
			after.start();
		} catch (ServletException | IOException e) {
			status.error("View did not render correctly: ", logger, e);
			throw new RuntimeException(e);
		}
	}

	public void prepareView() {
		if(node.getComplete()==TaskCompletion.OK) {
			status.info("This task is marked completed OK");
			return;
		}
		
		List<Method> candidates = com.trabajo.utils.AnnotationUtils.<StandardTaskLifecycle> getMethodsAnnotatedXWithParticluarEnumYForAttributeZ(taskObject.getClass(),
				TaskLifecycle.class, "phase", StandardTaskLifecycle.VIEW);
		
		if(candidates.size() > 1) {
			status.error("Task has more than one  method annotated @TaskLifeCycle(phase=VIEW)");
			throw new RuntimeException("Bad process definition");
		}
		if(candidates.size() == 0) {
			status.error("Task has no method annotated @TaskLifeCycle(phase=VIEW)");
			throw new RuntimeException("Bad process definition");
		}

		try {
			candidates.get(0).invoke(taskObject);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			status.error("Task phase VIEW threw an exception", logger, e);
			throw new RuntimeException("Unable to invoke task view", e);
		}
	}

	@Override
	public ITask getTask() {
		node.setLifecycle(this);
		return node;
	}

	@Override
	public Object getTaskObject() {
		return taskObject;
	}

	@Override
	public Object getProcessObject() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getTaskOrProcessObject() {
		return taskObject;
	}
	
	@Override
  public ITaskGroup createGroup(String name) {
		throw new IllegalStateException("Action not permitted in this context");
  }
}
