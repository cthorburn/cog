package com.trabajo.engine.bobj;

import java.util.Set;

import javax.persistence.EntityManager;

import com.trabajo.jpa.RoleJPA;
import com.trabajo.process.IRole;
import com.trabajo.process.IUser;

public class RoleImpl extends CogEntity<RoleJPA> implements IRole {
	
	public RoleImpl(EntityManager em, RoleJPA entity) {
		super(em, entity);
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

	@Override
	public Set<IUser> getUsers() {
		return Users.toImpl(em, entity().getUserSet());
	}

}
