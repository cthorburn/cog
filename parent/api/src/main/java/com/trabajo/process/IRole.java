package com.trabajo.process;

import java.util.Set;


public interface IRole extends IEntity {
	String getName();
	String getCategory();
	String getDescription();
	Set<IUser> getUsers();
}
