package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ServiceDefJPA;
import com.trabajo.process.IServiceDef;

public class ServiceDefImpl extends CogEntity<ServiceDefJPA> implements IServiceDef {

	public ServiceDefImpl(EntityManager em, ServiceDefJPA entity) {
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
	public void setDescription(String description) {
		entity().setDescription(description);
	}



	@Override
	public void deprecate(boolean deprecate) {
		entity().setDeprecated(deprecate);
		save();
	}


	@Override
	public void purge(EntityManager em) {
		em.remove(entity());
		em.flush();
	}


	@Override
  public boolean isDeprecated() {
	  return entity().isDeprecated();
  }
}
