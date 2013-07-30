<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", -1);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Trabajo!</title>

<link rel="stylesheet" type="text/css" href="css/main.css"/>

<link rel="stylesheet" type="text/css" href="js/dhtmlxLayout/codebase/dhtmlxlayout.css">
<link rel="stylesheet" type="text/css" href="js/dhtmlxLayout/codebase/skins/dhtmlxlayout_dhx_black.css">
<script src="js/dhtmlxMenu/codebase/dhtmlxcommon.js"></script>
<script src="js/dhtmlxLayout/codebase/dhtmlxlayout.js"></script>

<link rel="stylesheet" type="text/css" href="js/dhtmlxMenu/codebase/skins/dhtmlxmenu_dhx_black.css"/>
<script src="js/dhtmlxMenu/codebase/dhtmlxmenu.js"></script>
<script src="js/dhtmlxLayout/codebase/dhtmlxcontainer.js"></script>

<link rel="stylesheet" type="text/css" href="js/dhtmlxAccordion/codebase/skins/dhtmlxaccordion_dhx_black.css">
<script src="js/dhtmlxAccordion/codebase/dhtmlxaccordion.js"></script>

<link rel="stylesheet" type="text/css" href="js/dhtmlxGrid/codebase/dhtmlxgrid.css"/>
<link rel="stylesheet" type="text/css" href="js/dhtmlxGrid/codebase/dhtmlxgrid_skins.css"/>
<link rel="stylesheet" type="text/css" href="js/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_black.css"/>
<script src="js/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>
<script src="js/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>

<link rel="stylesheet" type="text/css" href="js/dhtmlxForm/codebase/skins/dhtmlxform_dhx_skyblue.css">
<script src="js/dhtmlxForm/codebase/dhtmlxform.js"></script>
<script src="js/dhtmlxForm/codebase/ext/dhtmlxform_item_container.js"></script>
<script src="js/dhtmlxForm/codebase/ext/dhtmlxform_item_upload.js"></script>

<link rel="stylesheet" type="text/css" href="js/dhtmlxCombo/codebase/dhtmlxcombo.css">
<script src="js/dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
    
<link rel="stylesheet" type="text/css" href="js/dhtmlxTabbar/codebase/dhtmlxtabbar.css">
<script  src="js/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>    

<link rel="stylesheet" type="text/css" href="js/dhtmlxTree/codebase/dhtmlxtree.css">
<script src="js/dhtmlxTree/codebase/dhtmlxtree.js"></script>
   
<<<<<<< HEAD
=======
<!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> -->
>>>>>>> be6e5a2b38666c17ec306afec472c771958263ca
<script src="js/jquery-min-1.9.1.js"></script>
<script src="js/trabajo/trabajo.js"></script>
<script src="js/trabajo/dhxtaskview.js"></script>
 
<c:if test="${formJavascriptInclude != null}">
	<c:forEach var="src" items="${formJavascriptInclude}">
		<script src="<c:out value="${src}" escapeXml="false"/>"></script>
	</c:forEach>
</c:if>
<c:if test="${formJavascript != null}">
	<c:forEach var="src" items="${formJavascript}">
		<script><c:out value="${src}" escapeXml="false"/></script>
	</c:forEach>
</c:if>

<script>
var formXML='<c:out value="${formXML}" escapeXml="false"/>';
var taskId=<c:out value="${taskId}"/>;
var form;
server=new Server();

$(document).ready(
		function() {
			form=new dhtmlXForm("form");
			form.setSkin("dhx_skyblue");
			form.attachEvent("onButtonClick", 
					
					function(name) {
						debugger;
						server.asyncServlet(
								{
									servlet: 'process', 
									action: 'dispose_task', 
									data: {tid: taskId, act: name },   
									context: {
										dispose_task_OK: function(response) {
											window.parent.location.href=response.jsonResult;
										}
									}
								}
						);
					}	
			);
			form.loadStructString(formXML);
		}
	);
</script>
	 
</head>
<body>
<div id="form"></div>
</body>
</html>