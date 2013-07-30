package com.trabajo.engine;

import java.io.Serializable;




public class ProcessExecutionResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public View view;
	public String type;
	public Serializable data;
	public TaskSummary taskSummary;
	
	public void setFormResult(String form) {
		type="FORM";
		data=form;		
	}
	public void setView(View view) {
		this.view=view;
		
	}
	public View getView() {
		return view;
	}
}
