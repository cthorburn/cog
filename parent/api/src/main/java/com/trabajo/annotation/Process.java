package com.trabajo.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;


@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE } )
@Inherited
public @interface Process {
	String name() default "";
	String version() default "";
	String category();
	String description();
}
