package com.trabajo.engine.bobj;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.TimerType;
import com.trabajo.jpa.NodeJPA;
import com.trabajo.jpa.NodeTimerJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.ITask;

public class NodeTimers {

	public static INodeTimer create(EntityManager em, String name, TimerType type, ITask task) {

		NodeTimerJPA nt = new NodeTimerJPA();
		nt.setName(name);
		nt.setTimerType(type);
		nt.setTask((NodeJPA)task.entity());
		em.persist(nt);
		em.flush();
		em.refresh(nt);
		return toImpl(em, nt);
	}
	
	
	public static void deleteByInstance(EntityManager em, IInstance instance) {
		TypedQuery<NodeTimerJPA> q=em.createQuery("select nt from NodeTimer nt inner join nt.task t where t.instance=?1", NodeTimerJPA.class);
		q.setParameter(1,  instance.entity());
		for(NodeTimerJPA j: q.getResultList()) {
			INodeTimer nt=NodeTimers.toImpl(em, j);
			nt.cancel();
			nt.delete();
		}
	}

	public static Set<INodeTimer> toImpl(EntityManager em, Collection<NodeTimerJPA> nts) {
		Set<INodeTimer> impls = new HashSet<>();
		for (NodeTimerJPA nt: nts) {
			impls.add(toImpl(em, nt));
		}
		return impls;
	}

	public static INodeTimer findById(EntityManager em, int id) {
		return toImpl(em, em.find(NodeTimerJPA.class, id));
	}

	public static INodeTimer toImpl(EntityManager em, NodeTimerJPA role) {
		return role==null ? null:new NodeTimerImpl(em, role);
	}
	
	public static Set<NodeTimerJPA> toEntity(Set<INodeTimer> impls) {
		Set<NodeTimerJPA> result = new HashSet<>(impls.size());
		for(INodeTimer impl: impls) {
			result.add((NodeTimerJPA)impl.entity());
		}
		return result;
	}

	public static INodeTimer findByNameTask(EntityManager em, String name, ITask task) {
		TypedQuery<NodeTimerJPA> q=em.createQuery("select nt from NodeTimer nt where nt.name=?1 and nt.task=?2", NodeTimerJPA.class);
		q.setParameter(1,  name);
		q.setParameter(2,  task.entity());
		try{
			return NodeTimers.toImpl(em, q.getSingleResult());
		}catch(NoResultException nre){
			return null;
		}
	}

	public static Collection<INodeTimer> findByTask(EntityManager em, ITask task) {
		TypedQuery<NodeTimerJPA> q=em.createQuery("select nt from NodeTimer nt where nt.task=?1", NodeTimerJPA.class);
		q.setParameter(1,  task.entity());
		return toImpl(em, q.getResultList());
	}

	public static void cancelAll(EntityManager em, Set<IInstance> instances) {
		for(IInstance instance: instances) {
			for(ITask task: Tasks.findByInstance(em, instance)) {
				Collection<INodeTimer> timers=findByTask(em, task);
				for(INodeTimer timer: timers) {
					timer.getHandle().getTimer().cancel();
				}
			}
		}
		
	}

}
