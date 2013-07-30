package com.trabajo.engine;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.DefinitionVersion;
import com.trabajo.process.ITask;


public class DHXView implements View {

	private String xml;
	private List<String> javascript;
	private List<String> javascriptInclude;
	
	private int taskId;
	private DefinitionVersion version;

	public DHXView(AbstractLifecycle lifecycle, String formName) {
		this.version=lifecycle.getProcessClassMetadata().getVersion();
		ITask task=	lifecycle.getTask();
		this.taskId=task.getId();
		xml = lifecycle.getJarMetadata().getTaskForm(task.getName(), formName);
		javascript = lifecycle.getJarMetadata().getJavascript(task.getName(), formName);
		javascriptInclude = lifecycle.getJarMetadata().getJavascriptInclude(task.getName(), formName);
	}

	public String getXml() {
		return xml;
	}

	@Override
	public String getURL(Object o, String correlator) {
		HttpServletRequest request = (HttpServletRequest) o;
		String server = request.getServerName();
		int port = request.getServerPort();
		String webContext = request.getServletContext().getContextPath();
		return "http://" + server + ":" + port +  webContext + "/process?a=view_task_2&view=view_" + correlator;
	}

	@Override
	public long getTaskId() {
		return taskId;
	}

	@Override
	public DefinitionVersion getVersion() {
		return version;
	}

	@Override
	public void dispose(TSession ts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("taskId", String.valueOf(taskId));
		request.setAttribute("formJavascript", javascript);
		request.setAttribute("formJavascript", javascript);
		request.setAttribute("formXML", xml);
		request.getServletContext().getRequestDispatcher("/dhxview_template.jsp").forward(request, response);
	}
}
