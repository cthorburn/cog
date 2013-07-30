package com.anyone;

import com.trabajo.process.ServiceImplFactory;
import com.trabajo.process.ServiceInfo;

public class ServiceFactory implements ServiceImplFactory {

	public Object createImpl(ServiceInfo config) {
		return new IssueSvcImpl(config);
	}
}
