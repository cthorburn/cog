package com.trabajo.engine.bobj;

import java.util.Collections;
import java.util.Set;

import javax.persistence.EntityManager;

import com.trabajo.jpa.GroupJPA;
import com.trabajo.jpa.RoleJPA;
import com.trabajo.jpa.UserJPA;
import com.trabajo.process.IGroup;
import com.trabajo.process.IRole;
import com.trabajo.process.IUser;

public class GroupImpl extends CogEntity<GroupJPA> implements IGroup {
	
	public GroupImpl(EntityManager em, GroupJPA entity) {
		super(em, entity, null);
	}

	@Override
	public Set<IUser> getMembers() {
		return Collections.unmodifiableSet(Users.toImpl(em, entity.getUserSet()));
	}

	@Override
	public Set<IRole> getRoles() {
		return Collections.unmodifiableSet(Roles.toImpl(em, entity().getRoleSet()));
	}

	@Override
	public void add(IUser user) {
		entity().getUserSet().add((UserJPA)user.entity());
		save();
		user.save();
	}

	@Override
	public void add(IRole role) {
		entity().getRoleSet().add((RoleJPA)role.entity());
		save();
		role.save();
	}

	@Override
	public void remove(IUser user) {
		entity().getUserSet().remove((UserJPA)user.entity());
		save();
		user.save();
	}

	@Override
	public void remove(IRole role) {
		entity().getRoleSet().remove((RoleJPA)role.entity());
		save();
		role.save();
	}

	@Override
	public String getName() {
		return entity().getName();
	}

	@Override
	public String getCategory() {
		return entity().getCategory();
	}

	@Override
	public String getDescription() {
		return entity().getDescription();
	}
}
