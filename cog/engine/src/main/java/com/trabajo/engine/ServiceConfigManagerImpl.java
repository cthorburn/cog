package com.trabajo.engine;

import com.trabajo.DefinitionVersion;
import com.trabajo.process.ServiceInfo;


public class ServiceConfigManagerImpl implements ServiceConfigManager {
    @SuppressWarnings("unused")
	private ProcessRegistry pr;
    
    public ServiceConfigManagerImpl(ProcessRegistry pr) {
        super();
        this.pr = pr;
    }

    @Override
    public ServiceInfo getServiceInfo(DefinitionVersion dv) {
    	throw new UnsupportedOperationException("implement me!");
    }
}
