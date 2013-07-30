package com.trabajo.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.ProcessException;
import com.trabajo.TaskCompletion;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.ITask;

public class Lifecycle_TIMEOUT extends AbstractLifecycle {
	
	private final static Logger logger = LoggerFactory.getLogger(Lifecycle_TIMEOUT.class);

	private Object taskObject;
	private ITask node;
	private INodeTimer timer;

	public Lifecycle_TIMEOUT(LifecycleContext context, ITask node, INodeTimer timer) {
		super(context);
		this.node=node;
		this.timer=timer;
		setInstance(node.getInstance());
		this.taskObject = createTaskObject(node);
		AnnotationHandlerChain before=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_TASK_TIMEOUT, this);
		before.setContext(this);
		before.start();
	}

	public void doTimeout() throws ProcessException  {
		if(node.getComplete()==TaskCompletion.OK) {
			status.info("This task is marked completed OK");
		}
		else {
			new TaskTimeoutRunner(taskObject, session(), node, timer.getTimerType()).run();
			node.save();
			AnnotationHandlerChain after=AnnotationHandlerFactory.forPoint(ApplicationPoint.AFTER_TASK_TIMEOUT, this);
			after.setContext(this);
			after.start();
		}
		timer.delete();
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
}
