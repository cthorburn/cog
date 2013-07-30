package com.trabajo.engine.bobj;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.trabajo.StateContainer;
import com.trabajo.jpa.NodeJPA;
import com.trabajo.jpa.TaskBarrierJPA;
import com.trabajo.process.ITask;
import com.trabajo.process.TaskCreator;
import com.trabajo.process.TaskCreator.Dependency;

public class TaskBarriers {

	public static void createAll(EntityManager em, NodeJPA node, List<Dependency> dependencies) {
		for(TaskCreator.Dependency dep: dependencies) {
			createDependency(em, node, dep);
		}
	}
	
	private static void createDependency(EntityManager em, NodeJPA node, Dependency dep) {
		TaskBarrierJPA j=new TaskBarrierJPA();
		j.setTask(node);
		j.setDependency(dep.getDepTaskName());
		j.setSatisfied(false);
		j.setChronology(dep.getChronology());
		em.persist(j);
	}

	public static Set<ITask> processEndTask(EntityManager em, ITask taskEnding, StateContainer parms) {

		String jpql="select tb from TaskBarrier tb where tb.dependency=?1 and (tb.dependencyTask=?2 or tb.dependencyTask IS NULL) and tb.task.instance=?3 and (tb.satisfied=false or tb.chronology=com.trabajo.process.TaskChronology.LATEST)";
		TypedQuery<TaskBarrierJPA> q=em.createQuery(jpql, TaskBarrierJPA.class);
		
		q.setParameter(1, taskEnding.getName());
		q.setParameter(2, taskEnding.entity());
		q.setParameter(3, taskEnding.getInstance().entity());
		
		Set<NodeJPA> startCandidates=new HashSet<>();
		for(TaskBarrierJPA tb: q.getResultList()) {
			startCandidates.add(tb.getTask());
			tb.setDependencyTask((NodeJPA)taskEnding.entity());
			tb.setParms(parms);
			tb.setSatisfied(true);
			em.persist(tb);
		}
		
		jpql="select DISTINCT tb.task from TaskBarrier tb where tb.task.instance=?1 and tb.satisfied=false";
		TypedQuery<NodeJPA> q2=em.createQuery(jpql, NodeJPA.class);
		q2.setParameter(1, taskEnding.getInstance().entity());
		
		startCandidates.removeAll(q2.getResultList());
		
		return Tasks.toImpl(em, startCandidates);
	}

	public static StateContainer getBarrierParms(EntityManager em, ITask node) {
		StateContainer result=new StateContainer();
		
		String jpql="select tb from TaskBarrier tb where tb.task=?1";
		
		TypedQuery<TaskBarrierJPA> q=em.createQuery(jpql, TaskBarrierJPA.class);
		
		q.setParameter(1, node.entity());
		
		for(TaskBarrierJPA tb: q.getResultList()) {
			result.put(tb.getDependency(), tb.getParms());
		}
		return result;
	}

	public static boolean satisfied(EntityManager em, ITask node) {
		boolean result=true;
		
		String jpql="select tb from TaskBarrier tb where tb.task=?1";
		
		TypedQuery<TaskBarrierJPA> q=em.createQuery(jpql, TaskBarrierJPA.class);
		
		q.setParameter(1, node.entity());

		for(TaskBarrierJPA tb: q.getResultList()) {
			if(!tb.getSatisfied()) {
				return false;
			}
		}
		return result;
	}

	public static boolean barriered(EntityManager em, ITask node) {
		String jpql="select count(tb) from TaskBarrier tb where tb.task=?1";
		TypedQuery<Long> q=em.createQuery(jpql, Long.class);
		q.setParameter(1, node.entity());
		return q.getSingleResult() > 0L;
	}

	public static void delete(EntityManager em, ITask task) {
		String jpql="delete from TaskBarrier tb where tb.task=?1 or tb.dependencyTask=?1";
		Query q=em.createQuery(jpql);
		q.setParameter(1, task.entity());
		q.executeUpdate();
	}
}
