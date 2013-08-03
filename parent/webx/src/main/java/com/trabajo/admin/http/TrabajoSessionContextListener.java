package com.trabajo.admin.http;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.trabajo.engine.DeferredMessages;
import com.trabajo.engine.TSession;

@WebListener
public class TrabajoSessionContextListener implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
    	arg0.getSession().setAttribute("tsession", new TSession());
    	arg0.getSession().setAttribute("deferredMessages", new DeferredMessages());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
      arg0.getSession().removeAttribute("session");
    }
}
