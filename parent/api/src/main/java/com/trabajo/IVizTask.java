package com.trabajo;

public interface IVizTask {
	void creates(String ... taskNames);
	void dependsOn(String ... taskNames);
	void canEndProcess();
	void setId(int id);
}
