package com.trabajo.engine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.DefinitionVersion;


public interface View {
	String getURL(Object request, String correlator);

	void dispose(TSession ts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	long getTaskId();

	DefinitionVersion getVersion();
}
