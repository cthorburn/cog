package com.trabajo.admin.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
 @Override
 protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  request.getSession(false).invalidate();
  response.sendRedirect(request.getContextPath());
 }
}