package com.trabajo.admin.http;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.DeferredMessages;
import com.trabajo.engine.EngineStatus;
import com.trabajo.engine.SysConfig;
import com.trabajo.engine.TSession;

/**
 * Servlet implementation class DHTMLXXML
 */
@WebServlet("/config")
public class ConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private EngineBean engine;
	
	@SuppressWarnings("unused")
	private final static Logger logger=LoggerFactory.getLogger(ConfigServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");

		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();
		EngineStatus status = ts.getStatus();

		switch (action) {
		case "fetch": {
			response.setContentType("application/json");
			try {
				engine.getConfig();
				response.getWriter().print(ts.getStatus().toJSONAddOK());
			} catch (Exception e) {
				status.deferMessages((DeferredMessages) request.getSession().getAttribute("deferredMessages"));
			}
			break;
		}
		case "update": {
			response.setContentType("application/json");
			SysConfig sc=engine.getConfig();
			sc.setGraphVizDir(request.getParameter("GraphVizDir"));
			sc.setFileCacheDir(request.getParameter("FileCacheDir"));
			sc.setRdbms(request.getParameter("rdbms"));
			sc.setDevelopment("1".equals(request.getParameter("development")));
			engine.updateConfig(sc);
			response.getWriter().print(ts.getStatus().toJSONAddOK());
			status.info("Config updated");
			status.deferMessages((DeferredMessages) request.getSession().getAttribute("deferredMessages"));
			break;
		}
		case "obliterate": {
			response.setContentType("application/json");
			try {
				SysConfig sc=engine.getConfig();
				sc.setGraphVizDir(request.getParameter("GraphVizDir"));
				sc.setFileCacheDir(request.getParameter("FileCacheDir"));
				sc.setRdbms(request.getParameter("rdbms"));
				sc.setObliterateOnRestart(Boolean.parseBoolean(request.getParameter("onoff")));
				engine.updateConfig(sc);
				response.getWriter().print(ts.getStatus().toJSONAddOK());
			} catch (Exception e) {
				status.deferMessages((DeferredMessages) request.getSession().getAttribute("deferredMessages"));
			}
			break;
		}
		}
}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
