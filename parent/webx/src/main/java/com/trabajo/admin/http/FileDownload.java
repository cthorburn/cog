package com.trabajo.admin.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.DeferredMessages;
import com.trabajo.engine.EngineImpl;
import com.trabajo.engine.EngineStatus;
import com.trabajo.engine.TSession;
import com.trabajo.utils.Strings;

//TODO catch & log all exceptions, return standard response

@WebServlet(name = "FileDownloadServlet", urlPatterns = { "/download" })
@MultipartConfig
public class FileDownload extends HttpServlet {

	@EJB
	private EngineBean engine;

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(EngineImpl.class);


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();
		EngineStatus status = ts.getStatus();


		switch (request.getParameter("type")) {
			case "report": {
				String before=Strings.beforeLast(request.getParameter("id"), '.');
				before=Strings.afterLast(before, '.');
				String after=Strings.afterLast(request.getParameter("id"), '.').replace('-', '.');
				String sd=before+"-"+after;
				
				DefinitionVersion dv=DefinitionVersion.parse(sd);
			  String s=engine.getReport(ts, dv);
			  
			  response.addHeader("Content-Disposition", "attachment; filename=report.xml");
        response.addHeader("Content-Length", ""+s.getBytes("UTF-8").length);
        response.setContentType("application/octet-stream");
				
        status.deferMessages((DeferredMessages) request.getSession().getAttribute("deferredMessages"));
				
        com.trabajo.utils.IOUtils.copyStream(new ByteArrayInputStream(s.getBytes("UTF-8")), response.getOutputStream());
				break;
			}
		}
	}
}