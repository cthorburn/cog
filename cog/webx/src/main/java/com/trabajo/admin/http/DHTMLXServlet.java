package com.trabajo.admin.http;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.trabajo.ejb.EngineBean;

@DeclareRoles("cog_user")
@ServletSecurity(@HttpConstraint(rolesAllowed = { "cog_user" }))
class DHTMLXServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	protected EngineBean engine;
	protected DHTMLX dhx;

	@Override
	public void init() {
		dhx = new DHTMLX(engine);
	}

	
	protected FeedParms getParms(String feed, HttpServletRequest request) {
		FeedParms fp = FeedParmsFactory.getJson(feed);
		if (fp != null)
			fp.parse(request.getParameterMap());
		return fp;
	}

	protected FeedParms getXMLParms(String feed, HttpServletRequest request) {
		FeedParms fp = FeedParmsFactory.getXml(feed);
		if (fp != null)
			fp.parse(request.getParameterMap());
		return fp;
	}

}
