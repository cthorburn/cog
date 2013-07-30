package com.trabajo.admin.http;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.EngineStatus;
import com.trabajo.engine.TSession;
import com.trabajo.process.Version;
import com.trabajo.utils.Strings;

/**
 * Servlet implementation class DHTMLXXML
 */
@WebServlet("/report")
public class ReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private EngineBean engine;
	
	private final static Logger logger=LoggerFactory.getLogger(ReportServlet.class);


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");
		
		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();
		
		EngineStatus status = ts.getStatus();
		ServletContext sc = request.getSession().getServletContext();
		
		switch (action) {
		case "deser": {
			response.setContentType("application/json");
			DefinitionVersion dv = parseTreeProcessComponent(request.getParameter("report"));
			String rptdef=engine.getReport(ts, dv);

			File reportDir=new File("C:\\SERVERS\\birt-runtime-4_2_2\\BIRT_VIEWER_WORKING_FOLDER");
			
			File report=new File(reportDir, request.getParameter("report")+".rptdesign");
			
			FileWriter fw=new FileWriter(report);
			fw.append(rptdef);
			fw.close();
			response.getWriter().print(status.toJSONAddOK());
			break;
		}	
		}
	}

	private DefinitionVersion parseTreeProcessComponent(String parameter) {
		Version v = Version.parse(Strings.afterLast(parameter, '.'), "-");
		String sn = Strings.beforeLast(parameter, '.');
		sn = Strings.afterLast(sn, '.');
		return new DefinitionVersion(sn, v);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
