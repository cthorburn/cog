package com.trabajo.engine.bobj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.NodeJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;

public class Tasks {

	public static ITask findById(EntityManager em, long id) {
		return toImpl(em, em.find(NodeJPA.class, id));
	}

	public static ITask toImpl(EntityManager em, NodeJPA node) {
		return new TaskImpl(em, node);
	}

	
	public static Set<ITask> toImpl(EntityManager em, Set<NodeJPA> set) {
		Set<ITask> impls = new HashSet<>(set.size());
		for (NodeJPA s : set) {
			impls.add(new TaskImpl(em, s));
		}
		return impls;
	}
	
	public static List<ITask> toImpl(EntityManager em, List<NodeJPA> set) {
		List<ITask> impls = new ArrayList<>(set.size());
		for (NodeJPA s : set) {
			impls.add(new TaskImpl(em, s));
		}
		return impls;
	}
	
	public static List<ITask> findByInstance(EntityManager em, IInstance instance) {
		TypedQuery<NodeJPA> q=em.createQuery("select n from Node n where n.instance=?1", NodeJPA.class); 
		q.setParameter(1,  instance.entity());
		return toImpl(em, q.getResultList());
	}

}
