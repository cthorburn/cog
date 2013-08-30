package com.trabajo;

public interface IVizInstance {

	void createsTasks(String ... names);
	void createsGroups(String ... names);

	IVizTask getTask(String name);

}
