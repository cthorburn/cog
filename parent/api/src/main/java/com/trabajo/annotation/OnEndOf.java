package com.trabajo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OnEndOf {
	OnEndOfType type();
	String inGroup() default "_default";
	OnEndOfMode mode();
}
