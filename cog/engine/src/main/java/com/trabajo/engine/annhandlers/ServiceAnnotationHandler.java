package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

import javax.jcr.RepositoryException;
import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;
import com.trabajo.annotation.Service;
import com.trabajo.engine.DVFactory;
import com.trabajo.engine.ProcessRegistry;
import com.trabajo.engine.ServiceDefs;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.process.ServiceImplFactory;
import com.trabajo.process.ServiceInfo;
import com.trabajo.process.Version;
import com.trabajo.utils.FieldVisitor;
import com.trabajo.utils.Visitation;
import com.trabajo.vcl.ClassLoaderManager;

@Component(
		applicationPoints={
				ApplicationPoint.BEFORE_INSTANCE_START, 
				ApplicationPoint.BEFORE_INSTANCE_END, 
				ApplicationPoint.BEFORE_TASK_CREATE, 
				ApplicationPoint.BEFORE_TASK_DATA, 
				ApplicationPoint.BEFORE_TASK_TIMEOUT,
				ApplicationPoint.BEFORE_TASK_VIEW,
				ApplicationPoint.BEFORE_TASK_DISPOSE 
		},
		dependencies={ 
				Dependency.ENTITY_MANAGER,  
				Dependency.PROCESS_VERSION,  
				Dependency.CLASSLOADER_MANAGER, 
				Dependency.TASK_OR_PROCESS_OBJECT, 
				Dependency.PROCESS_REGISTRY}
)
public class ServiceAnnotationHandler implements AnnotationHandler {
  	private EntityManager  ___em;
  	private ProcessRegistry ___processRegistry;
  	private Object ___taskOrProcessObject;
  	private DefinitionVersion ___processVersion;
  	
    private ClassLoaderManager<DefinitionVersion, DVFactory> ___classLoaderManager;
    
    @Override
    public void process() {
    	process(___taskOrProcessObject);
    }
    
    private void process(final Object o) {
    	
        new FieldVisitor(o, new  Visitation<Field>() {
        	private DefinitionVersion latestServiceVersion(EntityManager em, String serviceName) {
						return ServiceDefs.latestServiceVersion(em, serviceName);
        		
        	}
            @Override
            public void visit(Field it) {
              Service serviceAnn=it.getAnnotation(Service.class);
              if(serviceAnn!=null) {
                  try {
                  	DefinitionVersion sdv;
                  	
                  	String serviceName=serviceAnn.name();
                  	String serviceVersion=serviceAnn.version();
                  	
                  	if("".equals(serviceVersion)) {
                  		sdv=latestServiceVersion(___em, serviceName);
                  	}
                  	else {
                  		sdv=new DefinitionVersion(serviceName, Version.parse(serviceVersion));
                  	} 
                  
                    ServiceInfo si=new ServiceInfo(___processRegistry.getProcessServiceProperties(___processVersion).getServiceProperties(sdv));
                              
                    ServiceImplFactory<?> sif=(ServiceImplFactory<?>)___classLoaderManager.loadClass(si.getFactoryClassName()).newInstance();
                      
                    Object impl=sif.createImpl(si);
                    if(!it.isAccessible()) {
                        it.setAccessible(true);
                    }
                    it.set(o, impl);
                    
                  } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                      throw new RuntimeException(e); 
                  } catch (RepositoryException e) {
                    throw new RuntimeException(e); 
									}
              }
            }
        });
    }
    
}
