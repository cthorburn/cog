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
import com.trabajo.engine.EngineStatus;
import com.trabajo.engine.TSession;

@WebServlet("/map")
public class MapServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private EngineBean engine;
	
	@SuppressWarnings("unused")
	private final static Logger logger=LoggerFactory.getLogger(MapServlet.class);


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");

		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();
		EngineStatus status = ts.getStatus();

		switch (action) {
		case "user": {
			response.setContentType("application/json");
			engine.getCurrentUserMap(ts, "user_location");
			response.getWriter().print(status.toJSONAddOK());
			break;
		}
		case "updateLocMeta": {
			response.setContentType("application/json");
			engine.updateUserMapLocationMeta(ts, request.getParameter("location"), Double.parseDouble(request.getParameter("lat")), Double.parseDouble(request.getParameter("lng")), request.getParameter("name"), request.getParameter("value"));
			response.getWriter().print(status.toJSONAddOK());
			break;
		}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
