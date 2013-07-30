package com.trabajo.admin.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.ValidationException;
import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.ClassLoaderUploadManager;
import com.trabajo.engine.CogTaskStreamWriter;
import com.trabajo.engine.DeferredMessages;
import com.trabajo.engine.EngineStatus;
import com.trabajo.engine.TSession;
import com.trabajo.engine.View;
import com.trabajo.process.Version;
import com.trabajo.utils.Strings;

/**
 * Servlet implementation class DHTMLXXML
 */
@WebServlet("/process")
public class ProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private EngineBean engine;
	
	private final static Logger logger=LoggerFactory.getLogger(ProcessServlet.class);


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");

		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();
		EngineStatus status = ts.getStatus();

		switch (action) {
		case "processGraph": {
			response.setContentType("text/html");
			engine.getProcessGraph(ts, Integer.parseInt(request.getParameter("taskId")), response);
			break;
		}
		case "setProcessServiceProperty": {
			response.setContentType("application/json");
			engine.setProcessServiceProperty(ts, DefinitionVersion.parse(request.getParameter("processDv")), DefinitionVersion.parse(request.getParameter("serviceDv")), request.getParameter("key"), request.getParameter("value"));
			response.getWriter().print(status.toJSONAddOK());
			break;
		}
		case "create_role": {
			response.setContentType("application/json");
			try {
				engine.createRole(ts, request.getParameter("name"), request.getParameter("category"), request.getParameter("description"));
				status.info("Role created: " + request.getParameter("name"));
			} catch (EJBTransactionRolledbackException e) {
				status.exception("DUPLICATE_ROLENAME", request.getParameter("name"));
			}
			response.getWriter().print(status.toJSONAddOK());
			break;
		}
		case "create_group": {
			response.setContentType("application/json");
			try {
				engine.createGroup(ts, request.getParameter("name"), request.getParameter("category"), request.getParameter("description"));
				status.info("Group created: " + request.getParameter("name"));
			} catch (EJBTransactionRolledbackException e) {
				status.exception("DUPLICATE_GROUPNAME", request.getParameter("name"));
			}
			response.getWriter().print(status.toJSON());
			break;
		}
		case "starter_form": {
			response.setContentType("text/xml");
			engine.processGetStartupForm(ts, parseTreeProcessComponent(request.getParameter("dv")));
			response.getWriter().print((String) status.getResult());
			break;
		}

		case "start_process": {
			response.setContentType("application/json");
			Map<String, String> pParms = new HashMap<>();
			Map<String, String[]> hParms = request.getParameterMap();

			for (String key : hParms.keySet()) {
				if (key.startsWith("pp__")) {
					pParms.put(key.substring(4), hParms.get(key)[0]);
				}
			}
			engine.startProcess(ts, parseTreeProcessComponent(request.getParameter("dv")), pParms, request.getParameter("note"));
			response.getWriter().print(status.toJSON());
			break;
		}

		case "define_session_cl": {
			response.setContentType("application/json");
			DefinitionVersion dv = DefinitionVersion.parse(request.getParameter("dv"));
			ClassLoaderUploadManager clum = ts.getClassLoaderUploadManager();
			ClassLoaderUploadManager.ClassLoaderUpload clu = clum.get(dv);
			clum.remove(dv);
			engine.deployClassLoader(ts, clu);
			status.info("Classloader Defined: " + dv.toString());
			response.getWriter().print(status.toJSONAddOK());
			break;
		}

		case "define_session_sv": {
			response.setContentType("application/json");
			DefinitionVersion dv = DefinitionVersion.parse(request.getParameter("dv"));
			ClassLoaderUploadManager clum = ts.getClassLoaderUploadManager();
			ClassLoaderUploadManager.ClassLoaderUpload clu = clum.get(dv);
			clum.remove(dv);
			engine.deployService(ts, clu);
			response.getWriter().print(status.toJSONAddOK());
			break;
		}
		
		case "cl_named": {
			ClassLoaderUploadManager clum = ts.getClassLoaderUploadManager();
			try {
				clum.create(DefinitionVersion.parse(request.getParameter("dv")), request.getParameter("cat"), request.getParameter("desc"));
				response.getWriter().print(status.toJSONAddOK());
			} catch (ValidationException e) {
				status.error(e.getMessage());
			}
			break;
		}

		case "cl_order": {
			ClassLoaderUploadManager clum = ts.getClassLoaderUploadManager();
			clum.prioritise(Integer.parseInt(request.getParameter("sequence")), request.getParameter("dv"), Boolean.parseBoolean(request.getParameter("up")));
			response.getWriter().print(status.toJSONAddOK());
			break;
		}

		case "cl_del": {
			ClassLoaderUploadManager clum = ts.getClassLoaderUploadManager();
			clum.delete(request.getParameter("dv"), Integer.parseInt(request.getParameter("sequence")));
			response.getWriter().print(status.toJSONAddOK());
			break;
		}

		case "select_task": {
			engine.selectTask(ts, Long.parseLong(request.getParameter("tid")));
			response.getWriter().print(status.toJSON());
			break;
		}

		case "claim_task": {
			engine.claimTask(ts, Long.parseLong(request.getParameter("tid")));
			response.getWriter().print(status.toJSON());
			break;
		}

		case "view_task": {
			engine.viewTask(ts, Long.parseLong(request.getParameter("tid")));
			View view = (View) status.getResult();

			if (view == null || !status.isOk()) {
				status.deferMessages((DeferredMessages) request.getSession().getAttribute("deferredMessages"));
				request.getRequestDispatcher("dhxtasks.html").forward(request, response);
			} else {
				String correlator = String.valueOf(System.currentTimeMillis());
				request.getSession().setAttribute("view_" + correlator, view);
				request.setAttribute("taskIFrameUrl", view.getURL(request, correlator));
				request.getRequestDispatcher("/dhxtaskview.jsp").forward(request, response);
			}
			break;
		}


		case "view_task_2": {
			View view = (View) request.getSession().getAttribute(request.getParameter("view"));
			request.setAttribute("view", view);
			engine.viewTask2(ts, view, request, response);
			break;
		}
		
		case "cog_data": {
			int taskId=Integer.parseInt(request.getParameter("___task"));
			String name=request.getParameter("___name");
			String args=request.getParameter("___args");
			engine.writeTaskData(ts, taskId, name, args, response);
			break;
		}
		
		case "cog_resource": {
			int taskId=Integer.parseInt(request.getParameter("___task"));
			String path=request.getParameter("___path");
			String charSet=request.getParameter("___charset");
			String contentType=request.getParameter("___content_type");
			
			boolean binary=false;
			try{
				binary=Boolean.parseBoolean(request.getParameter("___binary"));
			}catch(Exception e){
			}

			boolean _static=false;
			try{
				_static=Boolean.parseBoolean(request.getParameter("___static"));
			}catch(Exception e){
			}
			DefinitionVersion dv = engine.versionForTaskId(ts, taskId);
			CogTaskStreamWriter ctsw=new CogTaskStreamWriter(dv, taskId, path, contentType, charSet, binary, _static);
			
			if(null!=ctsw.getCharSet()) {
				response.setCharacterEncoding(ctsw.getCharSet());
			}
			
			if(null!=ctsw.getContentType()) {
				response.setContentType(ctsw.getContentType());
			}

			if(ctsw.is_static() && ctsw.isBinary()) {
				ctsw.writeStatic(response.getOutputStream());
			}	
			else if(ctsw.is_static()) {
				ctsw.writeStatic(response.getWriter());
			}
			else {
				ctsw.write(response.getWriter());
			}
			
			break;
		}

		case "dispose_task": {
			long taskId = Long.parseLong(request.getParameter("___task"));
			engine.disposeTask(ts, taskId, request.getParameterMap(), request.getParameter("___name"));
			status.deferMessages((DeferredMessages) request.getSession().getAttribute("deferredMessages"));
			status.setJsonResult("dhxtasks.html");
			response.getWriter().print(status.toJSON());
			break;
		}
		case "deferred_msgs": {
			DeferredMessages dm = (DeferredMessages) request.getSession().getAttribute("deferredMessages");
			status.setMessages(dm.copyList());
			response.getWriter().print(status.toJSON());
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
