package com.trabajo.vcl;


public class ChainUpdate<T extends CLMKey<T>> {

	public static enum TYPE {DELETE, CHAIN_OUT_OF_SERVICE, CACHE_FILES_DELETED};
	
	private T dv;
	private TYPE type;

	public ChainUpdate(TYPE type, T dv) {
		super();
		this.type=type;
		this.dv = dv;
	}
	
	public T getClassLoaderVersion() {
		return dv;
	}
	public TYPE type() {
		return type;
	}
}
