package com.trabajo.engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/** Marker to eliminate MetadataScanner maintenance in respect 
 * of new engine related annotations. 
 * Just make sure everything annotated with any other engine 
 * annotation is also annotated with this! */ 
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE } )
@Inherited
public @interface Component {
	Dependency[] dependencies();
	ApplicationPoint[] applicationPoints();
}
