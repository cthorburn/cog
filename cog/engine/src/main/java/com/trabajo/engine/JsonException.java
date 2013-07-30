package com.trabajo.engine;

import java.io.Serializable;

import com.trabajo.engine.EngineStatus.Msg;

public class JsonException implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String detail;
	
	public String getName() {
		return name;
	}

	public String getDetail() {
		return detail;
	}

	public JsonException(String name, String detail) {
		super();
		this.name = name;
		this.detail = detail;
	}

	public Msg toMsg() {
		return new Msg("exception (deferred) name: "+name+" detail: "+detail);
	}
	
}
