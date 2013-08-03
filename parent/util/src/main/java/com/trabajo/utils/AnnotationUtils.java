package com.trabajo.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnnotationUtils {

	public static <V> List<Method> 
		getMethodsAnnotatedXWithParticluarEnumYForAttributeZ(
			Class<?> clazz, 
			Class<? extends Annotation> annotationClass, 
			String attrName, 
			V values) {

		return new MethodAnnotationMatcher(
		new MethodAnnotationMatchSpec[] {
			new EnumMethodAnnotationMatchSpec<V>(annotationClass, attrName, values)
		}).find(clazz);
	}

	public static List<Field> getFieldsAnnotated(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		Field[] fields = clazz.getDeclaredFields();
		if(fields.length==0)
			return Collections.emptyList();
		
		List<Field> result= new ArrayList<>();;
		for(Field f: fields) {
			if(f.isAnnotationPresent(annotationClass)) {
				result.add(f);
			}
		}
		return result;
	}
}

class MethodAnnotationMatcher {
	private MethodAnnotationMatchSpec[] specs;

	MethodAnnotationMatcher(MethodAnnotationMatchSpec[] specs){
		this.specs=specs;
	}
	
	List<Method> results=new ArrayList<>();
	
	public List<Method> find(Class<?> clazz) {
		for(Method m: clazz.getMethods()) {
			applyEachSpec(m, results);
		}
		
		return results;
	}

	private void applyEachSpec(Method m, List<Method> results) {
		for(MethodAnnotationMatchSpec spec: specs) {
			spec.apply(m, results);
		}
	}
}

interface MethodAnnotationMatchSpec {
	void apply(Method m, List<Method> results);
}

class EnumMethodAnnotationMatchSpec<V> implements MethodAnnotationMatchSpec {

	private String attrName;
	private Class<? extends Annotation> annotationClass;
	private V value;

	public EnumMethodAnnotationMatchSpec(Class<? extends Annotation> annotationClass, String attrName, V value) {
		this.annotationClass=annotationClass;
		this.attrName=attrName;
		this.value=value;
	}
	
	public void apply(Method m, List<Method> results) {
		Annotation a=m.getAnnotation(annotationClass);
		if(a!=null) {
			try {
				@SuppressWarnings("unchecked")
				V r=(V)a.getClass().getMethod(attrName).invoke(a);
				if(value==r) {
					results.add(m);
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				//ignore since it implies this class does not exhibit the annotation
			}
		}
	}
}
