package com.trabajo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE} )
public @interface FormItem {
	ControlType type();
	String name();
	String label();
	String value() default "";
	boolean  required() default true;
	String validate() default "";
	int rows() default 0;
}
