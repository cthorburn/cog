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
	public IVisualizer getVisualizer() {
		return entity().getVisualizer();
	}

	@Override
	public void setVisualizer(IVisualizer visualizer) {
		entity().setVisualizer((DAGVisualizer)visualizer);
		
	}
}
