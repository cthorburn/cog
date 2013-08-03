package com.trabajo.engine.bobj;

import java.util.Collections;
import java.util.Set;

import javax.persistence.EntityManager;

import com.trabajo.jpa.RoleJPA;
import com.trabajo.jpa.UserJPA;
import com.trabajo.process.IGroup;
import com.trabajo.process.IRole;
import com.trabajo.process.ITask;
import com.trabajo.process.IUser;

public class UserImpl extends CogEntity<UserJPA> implements IUser {
	
	public UserImpl(EntityManager em, UserJPA entity) {
		super(em, entity, null);
	}

	@Override
	public void assignTask(ITask task) {
		task.assignToUser(this);
	}

	@Override
	public Set<IRole> getRoles() {
		return Collections.unmodifiableSet(Roles.toImpl(em, entity.getRoles()));
	}

	@Override
	public Set<IGroup> getGroups() {
		return Collections.unmodifiableSet(Groups.toImpl(em, entity.getGroupSet()));
	}

	@Override
	public void addRole(IRole role) {
		entity().getRoleSet().add((RoleJPA)role.entity());
		save();
		role.save();
	}

	@Override
	public void removeRole(IRole role) {
		entity().getRoleSet().remove((RoleJPA)role.entity());
		save();
		role.save();
	}

	@Override
	public String getFullName() {
		return entity().getFullName();
	}

	@Override
	public String getLocale() {
		return entity().getLocale();
	}

	@Override
	public String getLangPref() {
		return entity().getLangPref();
	}

	@Override
	public String getEmail() {
		return entity().getPrimaryEmail();
	}

}
