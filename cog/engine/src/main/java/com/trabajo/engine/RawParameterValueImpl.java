package com.trabajo.engine;

import com.trabajo.RawParameterValue;

public class RawParameterValueImpl implements RawParameterValue {

	private String problem;
	private String[] value;
	private String name;

	@Override
	public String name() {
		return name;
	}

	public RawParameterValueImpl(String problem, String[] value) {
		super();
		this.problem = problem;
		this.value = value;
	}

	@Override
	public String[] value() {
		return value;
	}

	@Override
	public void setProblemString(String problem) {
		this.problem=problem;
	}

	@Override
	public String getProblemString() {
		return problem;
	}

}
