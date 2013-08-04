package com.trabajo.process;

import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;

public interface IClassLoaderDef extends IEntity {

	DefinitionVersion getDefinitionVersion();

	void setCategory(String value);

	void setDescription(String value);

	void deprecate(boolean deprecate);

	void purge(EntityManager em);
	boolean isDeprecated();
}
