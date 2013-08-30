package com.trabajo.process;

import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;
import com.trabajo.IVisualizer;

public interface IProcDef extends IEntity {

	DefinitionVersion getDefinitionVersion();

	void setCategory(String value);

	void setMainClass(String className);

	void setDescription(String value);


	void obliterate();

	void deprecate(boolean deprecate);

	void suspend(boolean suspend);

	void purge(EntityManager em);

	boolean isDeprecated();
	
	IVisualizer getVisualizer();
	void setVisualizer(IVisualizer viz);

}
