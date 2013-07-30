package com.trabajo.engine.bobj;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.RoleJPA;
import com.trabajo.process.IRole;
import com.trabajo.values.CreateRoleSpec;

public class Roles {

	public static IRole create(EntityManager em, CreateRoleSpec spec) {

		RoleJPA role = new RoleJPA();
		role.setName(spec.getName().getValue());
		role.setCategory(spec.getCategory().getValue());
		role.setDescription(spec.getDesc().getValue());
		em.persist(role);
		em.refresh(role);
		return toImpl(em, role);
	}

	public static Set<IRole> toImpl(EntityManager em, Set<RoleJPA> roles) {
		Set<IRole> impls = new HashSet<>();
		for (RoleJPA role : roles) {
			impls.add(toImpl(em, role));
		}
		return impls;
	}

	public static IRole findById(EntityManager em, int id) {
		return toImpl(em, em.find(RoleJPA.class, id));
	}

	public static IRole toImpl(EntityManager em, RoleJPA role) {
		return role==null ? null:new RoleImpl(em, role);
	}

	public static IRole findByName(EntityManager em, String rn) {
		String jpql = "select r from Role r where r.name=?1";
		TypedQuery<RoleJPA> q = em.createQuery(jpql, RoleJPA.class);
		q.setParameter(1, rn);
		try {
			return toImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public static Set<RoleJPA> toEntity(Set<IRole> impls) {
		Set<RoleJPA> result = new HashSet<>(impls.size());
		for(IRole impl: impls) {
			result.add((RoleJPA)impl.entity());
		}
		return result;
	}

}
