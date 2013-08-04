package com.trabajo.process;

import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;

public interface IServiceDef extends IEntity {

	DefinitionVersion getDefinitionVersion();

	void setCategory(String value);

	void setDescription(String value);

	void deprecate(boolean deprecate);
	boolean isDeprecated();

	void purge(EntityManager em);
}
