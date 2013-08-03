package com.trabajo.admin.http;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.trabajo.ValidationException;
import com.trabajo.ejb.SystemInitBean;

@WebListener
public class TrabajoServletContextListener implements ServletContextListener {

	@EJB
	private SystemInitBean systemInit;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		arg0.getServletContext().setAttribute("application", new Application());
		try {
			systemInit.init();
			new SystemVerification().verify();
		} catch (ValidationException e) {
			throw new RuntimeException(e);
		}
	}

}
