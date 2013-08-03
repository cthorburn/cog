package com.trabajo.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FieldVisitor {
	public FieldVisitor(Object o, Visitation<Field> v) {
		this(o.getClass(), v);
	}

	public FieldVisitor(final Class<?> c, final Visitation<Field> v) {

		List<Class<?>> supers;

		if (!c.getSuperclass().equals(Object.class)) {
			supers = new ArrayList<>();
			Class<?> spr = c;
			while (!spr.equals(Object.class)) {
				supers.add(spr);
			}
		} else {
			supers = Collections.<Class<?>> singletonList(c);
		}

		for (Class<?> cc : supers) {
			for (Field f : cc.getDeclaredFields()) {
				v.visit(f);
			}
		}
	}
}
