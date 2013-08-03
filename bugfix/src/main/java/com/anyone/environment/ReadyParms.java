package com.anyone.environment;

import java.util.Map;

import com.trabajo.ParameterAdapter;
import com.trabajo.RawParameterValue;

public class ReadyParms implements ParameterAdapter<ReadyParms> {

	private String env;
	
	@Override
	public void adapt(Map<String, RawParameterValue> parms, String action) {
		env=parms.get("env").value()[0];
	}

	@Override
	public boolean areAllIndividualParmsOK() {
		return true;	
	}

	@Override
	public boolean areParmsMutuallyConsistent() {
		return false;
	}

	@Override
	public String getInconsistencyDescription() {
		return null;
	}

	@Override
	public ReadyParms getAdapted() {
		return this;
	}

	
	public String env() {
		return env;
	}

	
	
}
