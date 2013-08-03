package com.trabajo;

public interface IVizInstance {

	void creates(String ... taskNames);

	IVizTask getTask(String taskName);

}
