package com.trabajo;

public interface IVizTaskContext {

	
	void createsTasks(String ... taskNames);
	void createsGroups(String ... taskNames);

	void canEndProcess();

	void dependsOn(String ... tasks);
	void dependsGroups(String ... groups);

}
