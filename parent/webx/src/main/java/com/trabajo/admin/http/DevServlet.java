package com.trabajo.admin.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ParameterParser;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.ejb.DevBean;
import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.DeferredMessages;
import com.trabajo.engine.EngineStatus;
import com.trabajo.engine.TSession;
import com.trabajo.process.Version;
import com.trabajo.utils.Strings;

/**
 * Servlet implementation class DHTMLXXML
 */
@WebServlet("/dev")
public class DevServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private DevBean dev;

	@EJB
	private EngineBean engine;

	private final static Logger logger = LoggerFactory.getLogger(DevServlet.class);

	private final static Path uploadPath = Paths.get(System.getProperty("user.home") + "\\.trabajo\\upload\\");

	static {
		logger.info("Dev servlet created!");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");

		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();
		EngineStatus status = ts.getStatus();

		Path target = null;
		String originalName = "none";
		long size = 0;
		Map<String, String> parms = null;

		if (action==null) {
			// assume upload

			response.setContentType("text/html");

			FileItemFactory factory = new DiskFileItemFactory();

			ServletContext servletContext = this.getServletConfig().getServletContext();

			File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

			((DiskFileItemFactory) factory).setRepository(repository);

			ServletFileUpload upload = new ServletFileUpload(factory);

			parms = new ParameterParser().parse(request.getQueryString(), '&');

			List<FileItem> items;
			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e) {
				throw new ServletException(e);
			}

			for (FileItem item : items) {
				String fieldName = item.getFieldName();
				if ("a".equals(fieldName)) {
					action = item.getString();
				} 
				else if ("rn".equals(fieldName)) {
					originalName = item.getString();
				} 
				else if ("file".equals(fieldName)) {
					try (InputStream is = item.getInputStream()) {

						String ext = ".jar";
						if (parms.get("type") != null) {
							switch (parms.get("type")) {
							case "task":
								ext = ".task_upload";
								break;
							case "report":
								ext = ".report";
								break;
							}
						}
						target = Files.createTempFile(uploadPath, "upload", ext);
						Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}

			try (FileInputStream ism = new FileInputStream(target.toFile())) {
				size = ism.getChannel().size();
			}

		}

		switch (action) {
		case "deploy_process": {
			doProcessUpload(ts, request, target, originalName, size);
			break;
		}
		
		case "deleteAllProcesses": {
			response.setContentType("application/json");
			dev.deleteAllProcesses(ts);
			response.getWriter().print((String) status.toJSONAddOK());
			break;
		}
			
		case "wipeQuartzTables": {
			response.setContentType("application/json");
			dev.wipeQuartzTables(ts);
			response.getWriter().print((String) status.toJSONAddOK());
			break;
		}
		
		default: {
			response.setContentType("application/json");
			status.error("No such action defined for DevServlet: "+action);
			response.getWriter().print((String) status.toJSONAddOK());
			break;
		}
		}
	}

	@SuppressWarnings("unused")
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

	private String doProcessUpload(TSession ts, HttpServletRequest request, Path target, String ofn, long size) {
		boolean ok = true;

		EngineStatus status = ts.getStatus();

		try {
			engine.deployProcess(ts, target.toFile(), ofn);
		} catch (RuntimeException e) {
			ok = false;
		}
		status.deferMessages((DeferredMessages) request.getSession().getAttribute("deferredMessages"));

		StringBuilder resp = new StringBuilder();
		resp.append("{ \"state\": " + ok + ", \"name\": \"");
		resp.append(target.toFile().getName());
		resp.append("\", \"size\": ");
		resp.append(size);
		resp.append('}');

		return resp.toString();
	}
}
