package com.trabajo.utils;

import java.lang.reflect.Method;

public class MethodVisitor {
    public MethodVisitor(Object o, Visitation<Method> v) {
        for(Method m: o.getClass().getDeclaredMethods()) {
            v.visit(m);
        } 
    }
}
