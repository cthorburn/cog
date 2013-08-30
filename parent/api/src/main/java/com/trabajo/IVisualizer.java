package com.trabajo;

/**
 * @author colin
 *
 */
public interface IVisualizer {


	/**
	 * Defines a group and the tasks it contains.
	 * 
	 * The default group is identified by the name "_default" and must be specified
	 * 
	 * @param groupName
	 * @param taskNames
	 */
	void groupTasks(String groupName, String ... taskNames);
	
	/** 
		returns a context on which subsequent operations act, on the given task, over each of the given groups
	*/
	IVizTaskContext task(String taskName, String ... groupNames);
	IVizTaskGroup group(String groupName);

	String status(IGraphEntityFinder  graphEntityFinder);
	String overview();

	IVizTaskContext start();
	
}
