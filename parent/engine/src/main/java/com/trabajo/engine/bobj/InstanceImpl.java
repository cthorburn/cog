package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.DAGVisualizer;
import com.trabajo.DefinitionVersion;
import com.trabajo.IVisualizer;
import com.trabajo.ProcessCompletion;
import com.trabajo.StateContainer;
import com.trabajo.jpa.InstanceJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.IProcDef;
import com.trabajo.process.ITask;
import com.trabajo.process.IUser;

public class InstanceImpl  extends CogEntity<InstanceJPA> implements IInstance {

	public InstanceImpl(EntityManager em, InstanceJPA entity) {
		super(em, entity, null);
	}

	@Override
	public IProcDef getProcDef() {
		return ProcDefs.toImpl(em, entity().getProcDef());
	}
	
	@Override
	public void end(ProcessCompletion way) {
		entity().setComplete(way);
		em.persist(entity());
		em.flush();
	}


	@Override
	public StateContainer getState() {
		return entity().getInstanceState();
	}

	@Override
	public void setState(StateContainer stateContainer) {
		entity().setInstanceState(stateContainer);
	}

	@Override
	public DefinitionVersion version() {
		return entity().getProcDef().getDefinitionVersion().toDefinitionVersion();
	}


	@Override
	public void purge() {
		for(ITask node: Tasks.findByInstance(em, this)) {
			node.purge();
		}
		em.remove(entity());
	}

	@Override
  public IUser getInitiator() {
	  return Users.toImpl(em, entity().getInitiator());
  }

}
