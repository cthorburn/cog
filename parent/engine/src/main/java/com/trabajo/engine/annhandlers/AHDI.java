package com.trabajo.engine.annhandlers;

import java.lang.reflect.Field;

public class AHDI {

	
	protected void set(AnnotationHandler ah, String name, Object o) {
		try {
			Field f=ah.getClass().getDeclaredField(name);
			if(!f.isAccessible()) {
				f.setAccessible(true);
			}
			f.set(ah, o);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
