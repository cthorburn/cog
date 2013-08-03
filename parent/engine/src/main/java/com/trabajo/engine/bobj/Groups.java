package com.trabajo.engine.bobj;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.GroupJPA;
import com.trabajo.process.IGroup;
import com.trabajo.values.CreateGroupSpec;

public class Groups {

	public static Set<IGroup> toImpl(EntityManager em, Set<GroupJPA> groups) {
		Set<IGroup> impls = new HashSet<>();
		for (GroupJPA group : groups) {
			impls.add(new GroupImpl(em, group));
		}
		return impls;
	}

	public static IGroup toImpl(EntityManager em, GroupJPA j) {
		return new GroupImpl(em, j);
	}

	public static IGroup findById(EntityManager em, int id) {
		return toImpl(em, em.find(GroupJPA.class, id));
	}

	public static Set<IGroup> findByTask(EntityManager em, TaskImpl taskImpl) {
		// TODO Auto-generated method stub
		return null;
	}

	public static IGroup create(EntityManager em, CreateGroupSpec spec) {
		GroupJPA group = new GroupJPA();
		group.setName(spec.getName());
		group.setCategory(spec.getCategory().getValue());
		group.setDescription(spec.getDesc().getValue());
		em.persist(group);
		em.refresh(group);
		return toImpl(em, group);
	}

	public static IGroup findByName(EntityManager em, String name) {
		String jpql = "select g from Group g where g.name=?1";
		TypedQuery<GroupJPA> q = em.createQuery(jpql, GroupJPA.class);
		q.setParameter(1, name);
		try {
			return toImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

}
