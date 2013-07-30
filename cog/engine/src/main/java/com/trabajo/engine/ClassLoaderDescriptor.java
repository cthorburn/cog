package com.trabajo.engine;

import java.util.List;
import java.util.Properties;

import com.trabajo.DefinitionVersion;
import com.trabajo.process.ServiceInfo;
import com.trabajo.values.Category;
import com.trabajo.values.Description;

public class ClassLoaderDescriptor {
	
	private DefinitionVersion version;
	private List<String> perProcessClasses;
	private String[] urls;
	private String[] originalJarNames;
	private Description description;
	private Category category;
	private Properties properties;
	
	
	public String[] getOriginalJarNames() {
		return originalJarNames;
	}

	public void setOriginalJarNames(String[] originalJarNames) {
		this.originalJarNames = originalJarNames;
	}

	
	public void setVersion(DefinitionVersion version) {
		this.version=version;
	}

	public DefinitionVersion  getVersion() {
		return version;
	}

	public void setPerProcessClasses(List<String> perProcessClasses) {
		this.perProcessClasses=perProcessClasses;
		
	}
	public List<String> getPerProcessClasses() {
		return perProcessClasses;
		
	}

	public void setDescription(Description description) {
		this.description=description;
	}
	public Description getDescription() {
		return description;
	}
	
	public void setCategory(Category category) {
		this.category=category;
	}
	public Category getCategory() {
		return category;
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public String getServiceFactoryClassName() {
		return properties.getProperty(ServiceInfo.PROP_IMPL_FACTORY_CLASS);
	}

	public void setProperties(Properties props) {
		this.properties=props;
	}
	
	public Properties getProperties() {
		return properties;
	}
}
