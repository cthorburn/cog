package com.trabajo.engine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.DefinitionVersion;
import com.trabajo.process.ITask;

public class HTMLView implements View {

	
	private ITask task;
	private DefinitionVersion version;
	private String path;
	private String charSet;
	private String contentType;
	private DefinitionVersion dv;
	
	public HTMLView(AbstractLifecycle lifecycle, String path) {
		this.version=lifecycle.getProcessClassMetadata().getVersion();
		this.dv=lifecycle.getInstance().getProcDef().getDefinitionVersion();
		task=	lifecycle.getTask();
		this.path=path;
		
	}


	@Override
	public String getURL(Object o, String correlator) {
		StringBuilder sb=new StringBuilder();
		sb.append("process?a=cog_resource&___task=");
		sb.append(task.getId());
		sb.append("&___path=");
		sb.append(path);
		sb.append("&___contentType=");
		sb.append(contentType);
		if(charSet!=null) {
			sb.append("&___charset=");
			sb.append(charSet);
		}
		
		return sb.toString();
	}

	@Override
	public void dispose(TSession ts, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CogTaskStreamWriter ctsw=new CogTaskStreamWriter(dv, task.getId(), path, "text/html", null, false, false);
		ctsw.write(response.getWriter());
	}

	@Override
	public long getTaskId() {
		return task.getId();
	}

	@Override
	public DefinitionVersion getVersion() {
		return version;
	}
}
