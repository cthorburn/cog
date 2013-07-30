package com.trabajo.vcl;

public interface ClassLoaderChainDefinition<T> {

	void revertClassLoader();

	T  key();

}
