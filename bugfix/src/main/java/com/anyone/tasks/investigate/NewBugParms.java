package com.anyone.tasks.investigate;

import java.util.Map;

import com.trabajo.ParameterAdapter;
import com.trabajo.RawParameterValue;

public class NewBugParms implements ParameterAdapter<NewBugParms> {

	@Override
	public void adapt(Map<String, RawParameterValue> parms, String action) {
		// TODO Auto-generated method stub
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
	public NewBugParms getAdapted() {
		return this;
	}

}
