package com.trabajo;

import org.slf4j.Logger;

import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;
import com.trabajo.process.ITaskGroup;
import com.trabajo.process.IUser;
import com.trabajo.process.TaskCreator;

public interface ILifecycle {
	void abandon(String reason);
	IInstance getInstance();
	ITask getTask();
	Logger logger();
	void setDHXView(String string);
	void setHTMLView(String url);

	ITask createTask(String taskName);
	ITask createTask(TaskCreator tc);
	ITask createTask(String taskName, ITaskGroup taskGroup);
	ITask createTask(TaskCreator tc, ITaskGroup taskGroup);

	ITask createTask(String taskName, StateContainer parms);
	ITask createTask(TaskCreator tc, StateContainer parms);
	ITask createTask(String taskName, StateContainer parms, ITaskGroup taskGroup);
	ITask createTask(TaskCreator tc, StateContainer parms, ITaskGroup taskGroup);

	void cancelTimer(String name);
	
	ITaskGroup createGroup(String name);
	void setProcessDisplayString(String string);
	IUser getProcessInitiator();
	
}
