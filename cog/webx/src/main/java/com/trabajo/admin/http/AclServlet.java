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
@WebServlet("/acl")
public class AclServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private EngineBean engine;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("a");
		TSession ts = (TSession) request.getSession().getAttribute("tsession");
		ts.newRequest();

		switch (action) {
		case "modify_group_roles": {
			response.setContentType("application/json");
			engine.modifyGroupRoles(ts, 
					Boolean.parseBoolean(request.getParameter("set")), 
					request.getParameter("groupName"), 
					Integer.parseInt(request.getParameter("roleid"))
					);
			response.getWriter().print(ts.getStatus().toJSON());
			break;
		}
		case "modify_group_users": {
			response.setContentType("application/json");
			engine.modifyGroupUsers(ts, 
					Boolean.parseBoolean(request.getParameter("set")), 
					request.getParameter("groupName"), 
					Integer.parseInt(request.getParameter("userid"))
					);
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
