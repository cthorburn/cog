package com.trabajo.engine;

public interface ProcessDependencies {

	void injectDependencies(ProcessClassMetadata pcm, Object process) throws IllegalArgumentException, IllegalAccessException;

}
