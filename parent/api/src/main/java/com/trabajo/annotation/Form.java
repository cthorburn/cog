package com.trabajo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD } )
@Inherited
public @interface Form {
	String name();
	String title();
	FormItem[] dataItems();
	String[] actionButtons(); 
	String[] javascript() default {}; 
	String[] javascriptInclude() default {}; 
}
