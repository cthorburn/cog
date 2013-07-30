package com.trabajo.process;

import com.trabajo.DefinitionVersion;

public interface IProcDef extends IEntity {

	DefinitionVersion getDefinitionVersion();

	void setCategory(String value);

	void setMainClass(String className);

	void setDescription(String value);


	void obliterate();
}
