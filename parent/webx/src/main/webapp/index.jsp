<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", -1);
%>

<fmt:setLocale value="en"/>
<fmt:setBundle basename="com.trabajo.i18n.Messages" var="lang"/>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trabajo!</title>

<link rel="stylesheet" href="css/main.css"/>

<script src="js/codebase/dhtmlx.js" type="text/javascript"></script> 
<link rel="stylesheet" type="text/css" href="js/codebase/dhtmlx.css"/> 

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script src="js/trabajo/mainmenu.js"></script>

<script>
debugger;
dhtmlxEvent(window,"load",function(){ 
	var menu = new dhtmlXMenuObject('menubar', 'dhx_skyblue');
	menu.attachEvent('onclick', menuClicked);
	menu.setIconsPath("img/");
	menu.loadXML('menu.jsp', function() {} );
}); 

//$(document).ready(function() {
//	debugger;
//	var menu = new dhtmlXMenuObject('menubar', 'dhx_skyblue');
//	menu.attachEvent('onclick', menuClicked);
//	menu.setIconsPath("img/");
//	menu.loadXML('menu.jsp', function() {} );
//});
</script>
	 
</head>
<body>
	<jsp:include page="banner.jsp"/>	
</body>
</html>