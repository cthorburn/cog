package com.trabajo;

import java.util.Map;
import java.util.Properties;

public interface IProcessServiceProperties {

	void addServiceProperties(DefinitionVersion serviceDv, Properties p);
	DefinitionVersion getProcessVersion();
	Map<DefinitionVersion, Properties> getMap();
	void setServiceProperty(DefinitionVersion serviceDv, String key, String value);
	Properties getServiceProperties(DefinitionVersion serviceDv);
}
