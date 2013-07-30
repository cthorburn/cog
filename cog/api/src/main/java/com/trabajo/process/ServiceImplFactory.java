package com.trabajo.process;

public interface ServiceImplFactory<T> {
    T createImpl(ServiceInfo config);
}
