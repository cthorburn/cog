package com.trabajo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Use this annotation to override the default ended process actions
 * 
 * These defaults are 
 * <ul>
 * <li>erase the database records</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE} )
public @interface OnCompletion {
	boolean erase() default true;
}
