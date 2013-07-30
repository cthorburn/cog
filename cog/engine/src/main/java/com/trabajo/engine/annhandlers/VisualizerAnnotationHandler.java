package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

import com.trabajo.ILifecycle;
import com.trabajo.annotation.Visualizer;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.utils.FieldVisitor;
import com.trabajo.utils.Visitation;

@Component(
		applicationPoints={
				ApplicationPoint.BEFORE_INSTANCE_START, 
				ApplicationPoint.BEFORE_TASK_CREATE, 
				ApplicationPoint.BEFORE_TASK_DATA, 
				ApplicationPoint.BEFORE_TASK_VIEW,
				ApplicationPoint.BEFORE_TASK_TIMEOUT,
				ApplicationPoint.BEFORE_TASK_DISPOSE 
		},
		dependencies={ 
				Dependency.LIFECYCLE,  
				Dependency.TASK_OR_PROCESS_OBJECT 
		}
)
public class VisualizerAnnotationHandler implements AnnotationHandler {
        
		private Object     ___taskOrProcessObject;
		private ILifecycle ___lifecycle;

    @Override
    public void process() {
    	process(___taskOrProcessObject, ___lifecycle);
    }
    
    private void process(final Object o, final ILifecycle l) {
    	
        new FieldVisitor(o, new  Visitation<Field>() {
            @Override
            public void visit(Field it) {
              Visualizer ann=it.getAnnotation(Visualizer.class);
              if(ann!=null) {
                  try {
                  	if(!it.isAccessible()) {
                  		it.setAccessible(true);
                  	}	
										it.set(o, l.getInstance().getVisualizer());
									} catch (IllegalArgumentException | IllegalAccessException e) {
										throw new RuntimeException(e);
									}
              }
            }
        });
    }
}
