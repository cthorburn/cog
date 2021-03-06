package com.trabajo.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE } )
@Inherited
public @interface Activation {
	String at() default "";
	boolean deprecatePreviousVersions() default false;
}
