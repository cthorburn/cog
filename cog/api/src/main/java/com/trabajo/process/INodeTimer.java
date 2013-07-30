package com.trabajo.process;

import javax.ejb.TimerHandle;

import com.trabajo.TimerType;

public interface INodeTimer extends IEntity {
	ITask getTask();
	TimerType getTimerType();
	TimerHandle getHandle();
	void cancel();
	void setHandle(TimerHandle handle);
	void delete();
}
