package com.trabajo.process;

import java.util.Properties;

public class ServiceInfo {
	
	public static final String PROP_IMPL_FACTORY_CLASS = "service-factory";

    private Properties props;
    
    public ServiceInfo(Properties props) {
        super();
        this.props = props;
    }

    public String getFactoryClassName() {
      return props.getProperty(PROP_IMPL_FACTORY_CLASS);
    }
    
    public String get(String key) {
      return props.getProperty(key);
    }
    
}    