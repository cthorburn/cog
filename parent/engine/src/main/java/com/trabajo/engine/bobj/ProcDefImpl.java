package com.trabajo.engine.bobj;

import java.util.Set;

import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ProcDefJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.IProcDef;
import com.trabajo.process.ITask;

public class ProcDefImpl extends CogEntity<ProcDefJPA> implements IProcDef {

	public ProcDefImpl(EntityManager em, ProcDefJPA entity) {
		super(em, entity, null);
	}


	@Override
	public DefinitionVersion getDefinitionVersion() {
		return entity().getDefinitionVersion().toDefinitionVersion();
	}

	@Override
	public void setCategory(String category) {
		entity().setCategory(category);

	}

	@Override
	public void setMainClass(String mainClass) {
		entity().setMainClass(mainClass);
	}

	@Override
	public void setDescription(String description) {
		entity().setDescription(description);
	}

	@Override
	public void obliterate() {
		Set<IInstance> instances=Instances.findByVersion(em, getDefinitionVersion());
		NodeTimers.cancelAll(em, instances);
		
		for(IInstance instance: instances) {
			 for(ITask task: Tasks.findByInstance(em, instance)) {
				 TaskBarriers.delete(em, task);
				 TaskUploads.delete(em, task);
				 em.remove(task.entity());
			 }
			 em.remove(instance.entity());
		}
	  em.remove(entity());
	}


	@Override
	public void deprecate(boolean deprecate) {
		entity().setDeprecated(deprecate);
		save();
	}


	@Override
	public void suspend(boolean suspend) {
		entity().setSuspended(suspend);
		save();
	}


	@Override
	public void purge(EntityManager em) {
		Instances.purge(em, getDefinitionVersion());
		em.remove(entity());
		em.flush();
	}


	@Override
  public boolean isDeprecated() {
	  return entity().isDeprecated();
  }
}
