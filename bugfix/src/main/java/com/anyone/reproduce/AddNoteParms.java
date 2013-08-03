package com.anyone.reproduce;

import java.util.Map;

import com.trabajo.ParameterAdapter;
import com.trabajo.RawParameterValue;

public class AddNoteParms implements ParameterAdapter<AddNoteParms> {

	private String note;
	
	@Override
	public void adapt(Map<String, RawParameterValue> parms, String action) {
		note=parms.get("note").value()[0];
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
	public AddNoteParms getAdapted() {
		return this;
	}

	
	public String note() {
		return note;
	}

	
	
}
