package com.trabajo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StateContainer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Serializable> map;
	
	public StateContainer() {
		map=new HashMap<String, Serializable>();
	}

	public void put(String key, Serializable value) {
		map.put(key, value);
	}
	
	public Serializable get(String key) {
		return map.get(key);
	}
}
