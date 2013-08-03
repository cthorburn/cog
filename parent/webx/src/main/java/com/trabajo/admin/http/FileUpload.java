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
import javax.servlet.annotation.MultipartConfig;
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
import com.trabajo.ValidationException;
import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.ClassLoaderUploadManager;
import com.trabajo.engine.DeferredMessages;
import com.trabajo.engine.EngineImpl;
import com.trabajo.engine.EngineStatus;
import com.trabajo.engine.TSession;
import com.trabajo.process.ITaskUpload;
import com.trabajo.process.Version;
import com.trabajo.values.Category;
import com.trabajo.values.Description;

//TODO catch & log all exceptions, return standard response

@WebServlet(name = "FileUploadServlet", urlPatterns = { "/upload" })
@MultipartConfig
public class FileUpload extends HttpServlet {

	@EJB
	private EngineBean engine;

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(EngineImpl.class);

	private final static Path uploadPath = Paths.get(System.getProperty("user.home") + "\\.trabajo\\upload\\");

	@Override
	public void init() throws ServletException {
		try {
			Files.createDirectories(uploadPath);
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();
		EngineStatus status = ts.getStatus();

		response.setContentType("text/html");

		FileItemFactory factory = new DiskFileItemFactory();

		ServletContext servletContext = this.getServletConfig().getServletContext();

		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

		((DiskFileItemFactory) factory).setRepository(repository);

		ServletFileUpload upload = new ServletFileUpload(factory);

		Map<String, String> parms = new ParameterParser().parse(request.getQueryString(), '&');

		List<FileItem> items;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}

		Path target = null;
		String originalName="none";
		
		for (FileItem item : items) {
			String fieldName = item.getFieldName();
			if ("file".equals(fieldName)) {
				try (InputStream is = item.getInputStream()) {

					String ext=".jar";
					switch(parms.get("type")) {
						case "task": ext=".task_upload"; break;
						case "report": ext=".report"; break;
					}
					
					originalName=item.getName();
					target = Files.createTempFile(uploadPath, "upload", ext);
					Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}

		String resp = null;

		long size = 0;
		try (FileInputStream ism = new FileInputStream(target.toFile())) {
			size = ism.getChannel().size();
		}

		switch (parms.get("type")) {
		case "pd":	{
			resp = doProcessUpload(ts, request, target, parms.get("rn"), size);
			break;
		}	
			
		case "report": {
				DefinitionVersion dv=new DefinitionVersion(parms.get("name"), Version.parse(parms.get("version"))); 
				engine.installReport(ts, target, dv, parms.get("cat"), parms.get("desc"), request.getRemoteUser(), originalName);
				resp=ts.getStatus().toJSON();
			break;
		}

		case "task": {
			int taskId = Integer.parseInt(parms.get("task"));
			String docSet= parms.get("docset");
			boolean versioned = false;

			try {
				versioned = Boolean.parseBoolean(request.getParameter("versioned"));
			} catch (Exception e) {
				// ignore
			}

			ITaskUpload taskUpload = engine.manageUpload(ts, target, taskId, originalName, docSet, request.getRemoteUser(), size, versioned);
			StringBuilder respx1 = new StringBuilder();
			respx1.append("{\"state\": true, \"name\": \"");
			respx1.append(taskUpload.getPath());
			respx1.append("\", \"size\": ");
			respx1.append(size);
			respx1.append("}");

			resp = respx1.toString();
			break;
		}	
		
		case "cl":
		case "sv": {  
			ClassLoaderUploadManager clum = ((TSession) request.getSession().getAttribute("tsession")).getClassLoaderUploadManager();

			boolean ok = true;
			ClassLoaderUploadManager.ClassLoaderUpload.JarUpload ju = null;

			try {
				ju = clum.addUpload(target, DefinitionVersion.parse(parms.get("dv")), false, parms.get("rn"), new Category(parms.get("cat")),
						new Description(parms.get("desc"), 255));
			} catch (ValidationException e) {
				ok = false;
				status.exception("VALIDATION", e.getMessage(), logger, e);
			}

			StringBuilder respx = new StringBuilder();
			respx.append("{\"state\": " + ok + ", \"name\": \"");
			respx.append(ju.sequence);
			respx.append("\", \"size\": ");
			respx.append(size);
			respx.append("}");

			resp = respx.toString();

			break;
		}	
	}

		response.getWriter().print(resp);
		
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