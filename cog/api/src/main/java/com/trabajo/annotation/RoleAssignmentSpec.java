package com.trabajo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAssignmentSpec {
    String name();
    String[] roleNames();
}
