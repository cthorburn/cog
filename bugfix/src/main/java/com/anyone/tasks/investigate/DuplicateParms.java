package com.anyone.tasks.investigate;

import java.util.Map;

import com.trabajo.ParameterAdapter;
import com.trabajo.RawParameterValue;

public class DuplicateParms implements ParameterAdapter<DuplicateParms> {

	private Integer existingId;
	private boolean ok;

	@Override
	public void adapt(Map<String, RawParameterValue> parms, String action) {
		RawParameterValue rpv=parms.get("existingId");
		
		if(rpv!=null) {
			try{
				existingId=Integer.valueOf(rpv.value()[0]);
			}
			catch(Exception e) {
				rpv.setProblemString("not pareseable");
				ok=false;
			}
		}	
	}

	@Override
	public boolean areAllIndividualParmsOK() {
		return ok;	
	}

	@Override
	public boolean areParmsMutuallyConsistent() {
		return ok;
	}

	@Override
	public String getInconsistencyDescription() {
		return "";
	}

	@Override
	public DuplicateParms getAdapted() {
		return this;
	}

	public Integer existingId() {
		return existingId;
	}
}
