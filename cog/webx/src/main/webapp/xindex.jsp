<%@page import="com.trabajo.engine.TSession"%>
<%
TSession ts=(TSession) session.getAttribute("tsession");
if(ts!=null) {
	ts.setRemoteUser(request.getRemoteUser());
}
response.sendRedirect("dhxtasks.html");
%>