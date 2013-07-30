package com.trabajo.admin.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.engine.TSession;

/**
 * Servlet implementation class DHTMLXXML
 */
@WebServlet("/json")
public class DHTMLXJSON extends DHTMLXServlet {
    
	private static final long serialVersionUID = 1L;
	 
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		String feed=request.getParameter("feed");
		response.getWriter().print(dhx.json((TSession)request.getSession().getAttribute("tsession"), feed, getParms(feed, request)));
	}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       doGet(request, response); 
    }
}
