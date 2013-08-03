package com.trabajo.engine;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.slf4j.Logger;

import com.trabajo.annotation.Inject;

public class MBeanManager {

	@Inject
	private Logger logger=null;
	private MBeanServer mbs;
	
	public MBeanManager() {
		mbs = ManagementFactory.getPlatformMBeanServer();
	}
	
	public void addEngine(Engine engine) {
        ObjectName name=null;
        
		try {
			name = new ObjectName(getMBeanName(engine));
	        mbs.registerMBean(engine, name); 
		
		} catch (InstanceAlreadyExistsException e) {
			logger.warn(Msgs.getString("MBeanRegistrationFailed", engine.getClass().getName(), name), e);
			
		} catch (MalformedObjectNameException | NotCompliantMBeanException | MBeanRegistrationException e) {
			throw new RuntimeException(e);
		} 
	}

	private String getMBeanName(Engine engine) {
		return engine.getClass().getAnnotation(SubsystemMetadata.class).objectName();
	}

	public void unregister(Engine engine) {
        ObjectName name=null;
        
		try {
			name = new ObjectName(getMBeanName(engine));
	        mbs.unregisterMBean(name); 
		} catch (MalformedObjectNameException | MBeanRegistrationException e) {
			throw new RuntimeException(e);
		} catch (InstanceNotFoundException e) {
			// ignore
		} 
	}
}
