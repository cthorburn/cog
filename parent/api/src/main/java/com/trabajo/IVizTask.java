package com.trabajo;

public interface IVizTask {
	void dependsOn(String ... taskNames);
	void canEndProcess();
	void setId(int id);
	void createsTasks(String ... taskNames);
	void createsGroups(String ... groupNames);
}
