package com.trabajo.process;

import java.util.Set;

public interface IUser  extends IEntity{
	void assignTask(ITask task);
	Set<IRole> getRoles();
	Set<IGroup> getGroups();
	void addRole(IRole r);
	void removeRole(IRole r);
	String getFullName();
	String getLocale();
	String getLangPref();
	String getEmail();
	
}
