package com.trabajo.engine.annhandlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.trabajo.DAGVisualizer;
import com.trabajo.ILifecycle;
import com.trabajo.IVisualizer;
import com.trabajo.annotation.Visualization;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.utils.MethodVisitor;
import com.trabajo.utils.Visitation;

@Component(
		applicationPoints={
				ApplicationPoint.BEFORE_INSTANCE_START 
		},
		dependencies={ 
				Dependency.LIFECYCLE,  
				Dependency.PROCESS_OBJECT 
		}
)
public class VisualizationAnnotationHandler implements AnnotationHandler {
        
		private Object     ___processObject;
		private ILifecycle ___lifecycle;

    @Override
    public void process() {
    	process(___processObject, ___lifecycle);
    }
    
    private void process(final Object o, final ILifecycle l) {
    	
    		IVisualizer viz=___lifecycle.getInstance().getProcDef().getVisualizer();
    		
    		if(viz==null) {
	        new MethodVisitor(o, new  Visitation<Method>() {
	            @Override
	            public void visit(Method it) {
	            	Visualization ann=it.getAnnotation(Visualization.class);
	            	if(ann!=null) {
		            	DAGVisualizer viz=new DAGVisualizer();
		            	try {
			              it.invoke(o, viz);
			            	___lifecycle.getInstance().getProcDef().setVisualizer(viz);
		              } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		              	//TODO __lifecycle.annHandlerError(e);
		              	throw new RuntimeException(e);
		              }
	            	}
	            }
	        });
    		}
    }
}
