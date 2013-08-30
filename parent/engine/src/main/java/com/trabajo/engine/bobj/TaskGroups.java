package com.trabajo.engine.bobj;

import java.util.GregorianCalendar;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.InstanceJPA;
import com.trabajo.jpa.NodeJPA;
import com.trabajo.jpa.TaskGroupJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITaskGroup;

public class TaskGroups {

	public static ITaskGroup create(EntityManager em, String name, IInstance instance) {
		TaskGroupJPA jpa=new TaskGroupJPA();
		
		jpa.setName(name);
		jpa.setInstance((InstanceJPA)instance.entity());
		jpa.setDescription("");
		jpa.setTaskGroupSequence(0);
		jpa.setComplete(null);
		jpa.setDateAssigned(new GregorianCalendar());
		jpa.setTasks(new HashSet<NodeJPA>());
		em.persist(jpa);
		em.flush();
		
	  return toImpl(em, jpa);
  }

	private static ITaskGroup toImpl(EntityManager em, TaskGroupJPA jpa) {
	  return new TaskGroupImpl(em, jpa);
  }

	public static ITaskGroup getDefaultGroup(EntityManager em, IInstance instance) {
	  TypedQuery<TaskGroupJPA> q=em.createQuery("select tg from TaskGroup tg where tg.instance=?1 and tg.name='_default'", TaskGroupJPA.class);
	  q.setParameter(1, instance.entity());
	  return toImpl(em, q.getSingleResult());
  }
}
