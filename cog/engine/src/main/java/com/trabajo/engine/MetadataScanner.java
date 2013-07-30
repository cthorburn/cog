package com.trabajo.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.trabajo.engine.annhandlers.AnnotationHandler;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.annotation.Component;

public class MetadataScanner {
	
	private Reflections reflections = new Reflections("com.trabajo.engine.annhandlers");
	private Set<Class<?>> annotated;

		public void process() {
		for(Class<?> clazz: annotated) {
			process(clazz);
		}
	}

	private void process(Class<?> clazz) {
		
	}

	public List<AnnotationHandler> handlersForPoint(ApplicationPoint ap) {
		List<AnnotationHandler> result=new ArrayList<>();
		
		Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);
		
		for(Class<?> clazz: components) {
			Component ann=clazz.getAnnotation(Component.class);
			for(ApplicationPoint apt: ann.applicationPoints()) {
				if(ap==apt) {
						try {
							result.add((AnnotationHandler) clazz.newInstance());
						} catch (InstantiationException | IllegalAccessException e) {
							throw new RuntimeException(e);
						}
				}
			}
		}
		
		return result;
	}

}