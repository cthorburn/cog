package com.trabajo.process;

import com.trabajo.DefinitionVersion;

public interface IReportDef extends IEntity {

	DefinitionVersion getDefinitionVersion();

	void setCategory(String value);

	String getReportText();

	void setDescription(String value);

	
}
