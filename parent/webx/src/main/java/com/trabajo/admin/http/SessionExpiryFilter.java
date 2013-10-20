package com.trabajo.admin.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(
		filterName= "sessionExpiry",
		urlPatterns={
	      "*.jsp",
	      "*.html",
	      "/xml",
	      "/json",
	      "/config",
	      "/form",
	      "/process",
	      "/user",
	      "/upload",
	      "/download",
	      "/report",
	      "/map"
		})
public class SessionExpiryFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		if (session != null && !session.isNew()) {
			chain.doFilter(request, response);
		} else {
			((HttpServletResponse) response).sendRedirect("/login.html");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
