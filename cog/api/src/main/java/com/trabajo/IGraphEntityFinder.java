package com.trabajo;

import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;

public interface IGraphEntityFinder {
	ITask findTaskById(int id);
	IInstance findInstanceById(int id);
}
