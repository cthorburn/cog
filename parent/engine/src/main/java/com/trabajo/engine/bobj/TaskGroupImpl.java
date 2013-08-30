package com.trabajo.engine.bobj;

import java.util.Set;

import javax.persistence.EntityManager;

import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.engine.AbstractLifecycle;
import com.trabajo.jpa.NodeJPA;
import com.trabajo.jpa.TaskGroupJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;
import com.trabajo.process.ITaskGroup;
import com.trabajo.process.TaskCreator;

public class TaskGroupImpl extends CogEntity<TaskGroupJPA> implements ITaskGroup {

	protected TaskGroupImpl(EntityManager em, TaskGroupJPA node) {
		super(em, node, null);
	}

	protected TaskGroupImpl(EntityManager em, TaskGroupJPA node, AbstractLifecycle l) {
		super(em, node, l);
	}
	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public IInstance getInstance() {
		return Instances.toImpl(em, entity().getInstance());
	}
	
	@Override
	public void purge() {
		em.remove(entity());
		em.flush();
	}

	@Override
  public ITask createTask(String taskName, StateContainer state) throws ProcessException {
	  return l.createTask(taskName, state, this);
  }

	@Override
  public ITask createTask(TaskCreator tc, StateContainer state) throws ProcessException {
	  return l.createTask(tc, state, this);
  }

	@Override
  public void addTask(ITask task) {
		Set<NodeJPA> all=entity().getTasks();
		
		int cnt=0;
		for(NodeJPA jpa: all) {
			if(jpa.getName().equals(task.getName())) {
				cnt++;
			}
		}
		NodeJPA tJpa=(NodeJPA)task.entity();
		tJpa.setTaskGroup(entity());
		tJpa.setGroupSequence(cnt);
	  em.persist(tJpa);
		
	  entity().getTasks().add(tJpa);
	  em.persist(entity());
	  em.flush();
  }
}
