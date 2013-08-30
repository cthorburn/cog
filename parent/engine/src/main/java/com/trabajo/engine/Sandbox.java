package com.trabajo.engine;

import java.io.IOException;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.IProcessServiceProperties;
import com.trabajo.jpa.ProcessJarAnalysis;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.ITask;

public interface Sandbox {
	void setMetadata(ProcessClassMetadata pcm, ProcessJarAnalysis wjm);
	void start(Map<String, String> hParms, String note);
	void selectTask(ITask  node);
	void viewTask(ITask node);
	void viewTask2(ITask node, View view, HttpServletRequest request, HttpServletResponse response);
	void disposeTask(ITask node, Map<String, String[]> parameterMap, String action);
	void writeTaskData(ITask node, String name, String args, HttpServletResponse response);
	void taskTimeout(INodeTimer nodeTimer);
	IProcessServiceProperties getProcessServiceProperties() throws RepositoryException, IOException;
}
