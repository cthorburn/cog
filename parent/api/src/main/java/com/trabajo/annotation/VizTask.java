package com.trabajo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE } )
public @interface VizTask {
	String[] createsTasks() default {};
	String[] createsGroups() default {};
	String[] dependsOnTasks() default {};
	String[] dependsOnGroups() default {};
}
