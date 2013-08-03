package com.trabajo;

import java.util.Map;

public interface ParameterAdapter<T> {
	
	/**
	 * Adapt/validate the supplied parameters with respect to the given action 
	 * @param parms
	 * @param action
	 */
	void adapt(Map<String, RawParameterValue> parms, String action);

	/**
	 * called after adapt()
	 * @return false if ANY parameter format is bad (set the problem on the RawParameterValue during adapt) 
	 */
	boolean areAllIndividualParmsOK();

	/**
	 * called only if areAllIndividualParmsOK() returns true;
	 * 
	 * @return true if the logical collection of (adapted) parameters would be considered self consistent by the eventual consumer. 
	 */
	boolean areParmsMutuallyConsistent();
	
	/**
	 * called if areParmsMutuallyConsistent() returns false
	 * 
	 * @return a string describing inconsistencies between raw parameters
	 */
	String getInconsistencyDescription();

	
	/**
	 * @return null if either areAllIndividualParmsOK() and areParmsMutuallyConsistent() return false. Otherwise an Object representing the fully parsed validated and adapted raw parameters
	 */
	T getAdapted();
}
