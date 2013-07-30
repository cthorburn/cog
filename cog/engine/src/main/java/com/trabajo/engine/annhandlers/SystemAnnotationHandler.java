package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

import com.trabajo.ILifecycle;
import com.trabajo.annotation.ACog;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.utils.FieldVisitor;
import com.trabajo.utils.Visitation;

@Component(
		applicationPoints={
				ApplicationPoint.BEFORE_INSTANCE_START, 
				ApplicationPoint.BEFORE_INSTANCE_END, 
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
public class SystemAnnotationHandler implements AnnotationHandler {
    
		private ILifecycle 	___lifecycle;
		private Object 			___taskOrProcessObject;
	
	
		@Override
		public void process() {
			process(___taskOrProcessObject, ___lifecycle);
		}
	
      private void process(final Object taskOrProcess, final ILifecycle l) {
          
          new FieldVisitor(taskOrProcess, new  Visitation<Field>() {
            @Override
            public void visit(Field it) {
            	ACog systemAnn=it.getAnnotation(com.trabajo.annotation.ACog.class);
              if(systemAnn!=null) {

                if(!it.isAccessible()) {
                  it.setAccessible(true);
                }
                try {
  								it.set(taskOrProcess, l);
  							} catch (IllegalArgumentException | IllegalAccessException e) {
  								throw new RuntimeException(e);
  							}
              }
            }
        });
    }
}
