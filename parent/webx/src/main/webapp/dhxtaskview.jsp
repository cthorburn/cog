<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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

<script src="js/codebase/dhtmlx.js" type="text/javascript"></script> 
<link rel="stylesheet" type="text/css" href="js/codebase/dhtmlx.css"/> 
   
<script src="js/jquery-min-1.9.1.js"></script>
<script src="js/trabajo/trabajo.js"></script>
<script src="js/trabajo/dhxtaskview.js"></script>

<style>
fieldset {margin: 25px;}
</style>

<script>
var layout;	
var taskURL='<c:out value="${taskIFrameUrl}" escapeXml="false"/>';

debugger;
$(document).ready(
	function() {
	    documentLoaded();
		layout=buildLayout();
	}
);

function buildLayout() {
	layout=new TrabajoLayout("Tasks", "2E", ["Task", "task", "Status", "status"]);
	
	buildCellStatus(layout.cell("status"));
	buildCellTask(layout.cell("task"));
}

function buildCellTask(layoutCell) {
	layoutCell.setText("Task");
	layoutCell.attachURL(taskURL);
}

function buildCellStatus(cell) {
    cell.setHeight("250");
	initLogger(cell);
	logger.log("Loaded OK");
}


</script>
	 
</head>
<body>
<div id="cog_header" style="height: 50px;"></div>
<div id="layout" style="position: relative; top: 20px; left: 20px; width: 600px; height: 800px; aborder: #B5CDE4 1px solid;"></div>
<div id="status">
	<p>Some Status!</p>
</div>
</body>
</html>