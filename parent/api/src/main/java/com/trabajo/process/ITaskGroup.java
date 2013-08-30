package com.trabajo.process;

import com.trabajo.StateContainer;

public interface ITaskGroup extends IEntity  {
	String getName();

	IInstance getInstance();

	void purge();

	ITask createTask(String string, StateContainer parms);
	ITask createTask(TaskCreator tc, StateContainer parms);

	void addTask(ITask task);
}
