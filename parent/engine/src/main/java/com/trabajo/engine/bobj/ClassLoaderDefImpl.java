package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ClassLoaderDefJPA;
import com.trabajo.process.IClassLoaderDef;

public class ClassLoaderDefImpl extends CogEntity<ClassLoaderDefJPA> implements IClassLoaderDef {

	public ClassLoaderDefImpl(EntityManager em, ClassLoaderDefJPA entity) {
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
