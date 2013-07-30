package com.trabajo.process;

import java.util.GregorianCalendar;
import java.util.Set;

import com.trabajo.DeadlineTimescale;
import com.trabajo.StateContainer;
import com.trabajo.TaskCompletion;
import com.trabajo.TaskStatus;
import com.trabajo.annotation.Visualizer;


public interface ITask  extends IEntity {

	void assignToUser(String username);
	void assignToUser(IUser user);
	IUser getUserAssignedTo();
	
	void assignToRoles(String... rolenames);
	void assignToRoles(Set<IRole> roles);
	Set<IRole> getRolesAssignedTo();
	
	void end(TaskCompletion way, StateContainer parms);
	TaskCompletion getComplete();
	String getName();
	IInstance getInstance();
	String getNodeClassName();
	StateContainer getState();  //TODO cannot give this method to tasks it must be got thru injection 
	void setStateContainer(StateContainer stateContainer);
	void setStatus(TaskStatus status);
	TaskStatus getStatus();
	void setDeadline(int i, DeadlineTimescale days);
	GregorianCalendar getDueDate();
	IUser getOwner();
	void linkWithVisualizer();
}
