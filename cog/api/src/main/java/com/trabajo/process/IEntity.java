package com.trabajo.process;

import com.trabajo.ILifecycle;

public interface IEntity {
	int getId();
	void save();
	Object entity();
	String toJson();
	void setLifecycle(ILifecycle  lifecycle);
}
