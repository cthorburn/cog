package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

import com.trabajo.ILifecycle;
import com.trabajo.annotation.ProcessState;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;
import com.trabajo.engine.annotation.Dependency;
import com.trabajo.utils.FieldVisitor;
import com.trabajo.utils.Visitation;

@Component(
		applicationPoints={
				ApplicationPoint.BEFORE_INSTANCE_START,
				ApplicationPoint.BEFORE_INSTANCE_END
		},
		dependencies={ Dependency.LIFECYCLE,  Dependency.PROCESS_OBJECT}
)
public class ProcessStateAnnotationHandler implements AnnotationHandler {
        
		private Object     ___processObject;
		private ILifecycle ___lifecycle;

    @Override
    public void process() {
    	process(___processObject, ___lifecycle);
    }
    
    private void process(final Object o, final ILifecycle l) {
    	
        new FieldVisitor(o, new  Visitation<Field>() {
            @Override
            public void visit(Field it) {
              ProcessState ann=it.getAnnotation(ProcessState.class);
              if(ann!=null) {
                  try {
                  	if(!it.isAccessible()) {
                  		it.setAccessible(true);
                  	}	
										it.set(o, l.getInstance().getState());
									} catch (IllegalArgumentException | IllegalAccessException e) {
										throw new RuntimeException(e);
									}
              }
            }
        });
    }
}
