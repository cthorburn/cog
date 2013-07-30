package com.trabajo.admin.http;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.TSession;
import com.trabajo.engine.UserPrefs;

/**
 * Servlet implementation class DHTMLXXML
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private EngineBean engine;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");
		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();

		switch (action) {
		case "create": {
			response.setContentType("application/json");
			engine.createUser(ts, request.getParameter("fullName"), request.getParameter("email"), request.getParameter("username"), request.getParameter("password"));
			response.getWriter().print(ts.getStatus().toJSON());
			break;
		}
		case "modify_role": {
			response.setContentType("application/json");
			engine.modifyRole(ts, 
					Boolean.parseBoolean(request.getParameter("set")), 
					Integer.parseInt(request.getParameter("userid")), 
					Integer.parseInt(request.getParameter("roleid"))
					);
			response.getWriter().print(ts.getStatus().toJSON());
			break;
		}
		case "modify_group": {
			response.setContentType("application/json");
			engine.modifyGroup(ts, 
					Boolean.parseBoolean(request.getParameter("set")), 
					Integer.parseInt(request.getParameter("userid")), 
					Integer.parseInt(request.getParameter("groupid"))
					);
			response.getWriter().print(ts.getStatus().toJSON());
			break;
		}
		case "get_prefs": {
			response.setContentType("application/json");
			UserPrefs up=new GsonHTTP(request, new String[] {"a"}).get(UserPrefs.class);
			
			engine.setUserPrefs(ts, up); 
			response.getWriter().print(ts.getStatus().toJSON());
			break;
		}
		}
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
