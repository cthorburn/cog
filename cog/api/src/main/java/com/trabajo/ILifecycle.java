package com.trabajo;

import org.slf4j.Logger;

import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;
import com.trabajo.process.TaskCreator;

public interface ILifecycle {
	void abandon(String reason);
	IInstance getInstance();
	ITask getTask();
	Logger logger();
	void setDHXView(String string);
	void setHTMLView(String url);

	ITask createTask(String string, StateContainer parms) throws ProcessException;
	ITask createTask(TaskCreator tc, StateContainer parms) throws ProcessException;
	void cancelTimer(String name);
	
}
