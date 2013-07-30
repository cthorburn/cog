package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

import com.trabajo.ILifecycle;
import com.trabajo.annotation.AInstance;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.process.IInstance;
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
public class InstanceAnnotationHandler implements AnnotationHandler {
    
			private Object     ___taskOrProcessObject;
			private ILifecycle ___lifecycle;
		
		  @Override
		  public void process() {
		  	process(___taskOrProcessObject, ___lifecycle);
		  }
    
      private void process(final Object taskOrProcess, final ILifecycle l) {
          
          new FieldVisitor(taskOrProcess, new  Visitation<Field>() {
            @Override
            public void visit(Field it) {
            	AInstance ann=it.getAnnotation(com.trabajo.annotation.AInstance.class);
              if(ann!=null) {
              	IInstance inject=l.getInstance();

                if(!it.isAccessible()) {
                  it.setAccessible(true);
                }
                try {
  								it.set(taskOrProcess, inject);
  							} catch (IllegalArgumentException | IllegalAccessException e) {
  								throw new RuntimeException(e);
  							}
              }
            }
        });
    }
}
