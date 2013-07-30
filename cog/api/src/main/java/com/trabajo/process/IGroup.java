package com.trabajo.process;

import java.util.Set;

public interface IGroup extends IEntity  {
	Set<IUser> getMembers();
	Set<IRole> getRoles();
	
	void add(IUser user);
	void add(IRole role);
	
	void remove(IUser user);
	void remove(IRole role);
	String getName();
	String getCategory();
	String getDescription();
}
