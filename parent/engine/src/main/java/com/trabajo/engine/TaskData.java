package com.trabajo.engine;

import java.io.OutputStream;
import java.io.PrintWriter;

public class TaskData {

	private String outputMethod;
	private String contentType;
	private Object content;

	public TaskData(String outputMethod, String contentType) {
		super();
		this.outputMethod = outputMethod;
		this.contentType=contentType;
	}

	public void setContent(Object content) {
		this.content=content;
	}
	
	public String getOutputMethod() {
		return outputMethod;
	}

	public String getContentType() {
		return contentType;
	}

	public void write(OutputStream out) {
		throw new UnsupportedOperationException("subclass me!");
	}

	public void write(PrintWriter writer) {
		writer.write((String)content);
		
	}
}
