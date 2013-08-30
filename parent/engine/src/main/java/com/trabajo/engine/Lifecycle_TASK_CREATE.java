package com.trabajo.engine;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;

import javax.ejb.ScheduleExpression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.TaskCompletion;
import com.trabajo.TaskStatus;
import com.trabajo.TimerType;
import com.trabajo.annotation.StandardTaskLifecycle;
import com.trabajo.annotation.Timeout;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.bobj.TaskBarriers;
import com.trabajo.engine.bobj.TaskGroups;
import com.trabajo.process.ITask;
import com.trabajo.process.ITaskGroup;
import com.trabajo.utils.MethodVisitor;
import com.trabajo.utils.Visitation;

public class Lifecycle_TASK_CREATE extends AbstractLifecycle {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(Lifecycle_TASK_CREATE.class);

	private Object taskObject;
	private ITask task;

	public Lifecycle_TASK_CREATE(LifecycleContext context, ITask task) {
		super(context);
		this.taskObject = createTaskObject(task);
		this.task = task;
		task.linkWithVisualizer();
		setInstance(task.getInstance());
	}

	public void execute() throws ProcessException {

		if (task.getComplete() == TaskCompletion.OK) {
			status.info("This task is marked completed OK");
			return;
		}

		StateContainer parms = task.getState();

		if (TaskBarriers.barriered(em, task)) {
			task.setStatus(TaskStatus.BARRIERED);
			if (TaskBarriers.satisfied(em, task)) {
				parms = TaskBarriers.getBarrierParms(em, task);

				AnnotationHandlerChain before=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_TASK_CREATE, this);
				before.setContext(this);
				before.start();

				new TaskPhaseRunner(taskObject, StandardTaskLifecycle.CREATE, session(), parms, task).run();
				task.setStatus(TaskStatus.CREATED);
				
				AnnotationHandlerChain after=AnnotationHandlerFactory.forPoint(ApplicationPoint.AFTER_TASK_CREATE, this);
				after.setContext(this);
				after.start();
			}
		} else {
			new TaskPhaseRunner(taskObject, StandardTaskLifecycle.CREATE, session(), parms, task).run();
			task.setStatus(TaskStatus.CREATED);
		}

		if (task.getStatus() == TaskStatus.CREATED) {
			scanForScheduling(task.getDueDate());
		}

		task.save();
	}

	private void scanForScheduling(final GregorianCalendar dueDate) {
		new MethodVisitor(taskObject, new Visitation<Method>() {
			@Override
			public void visit(Method m) {
				Timeout timeout = m.getAnnotation(Timeout.class);
				if (timeout == null) {
					return;
				}
				ScheduleExpression expr = new TimeoutExpression(timeout).expr(dueDate);

				if (timeout.type() == TimerType.DUEDATE) {
					status.getTaskScheduling().add(new TaskDueDate(timeout.name(), expr, task));
				}
			}
		});
	}

	@Override
	public ITask getTask() {
		task.setLifecycle(this);
		return task;
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
	  return TaskGroups.create(em, name, getInstance());
  }

}
