<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Trabajo!</title>
<link rel="stylesheet" href="css/main.css"/>
<link rel="stylesheet" href="js/dhx/dhtmlxMenu/codebase/skins/dhtmlxmenu_dhx_skyblue.css"/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

<script src="js/dhx/dhtmlxMenu/codebase/dhtmlxcommon.js"></script>
<script src="js/dhx/dhtmlxMenu/codebase/dhtmlxmenu.js"></script>


<script>

dhtmlxEvent(window, "load", buildInterface);

function buildInterface() {
	var menu = new dhtmlXMenuObject('menubar', 'dhx_skyblue');
	menu.setIconsPath("../imgs/");
	var str='<menu><item id="file" text="File"><item id="new" text="New" /><item id="ms1" type="separator" /><item id="export" text="Export"><item id="export_pdf" text="PDF" /></item></item></menu>';
	menu.loadXMLString(str, function(){
	    alert('loaded!');
	});
	
}


$(document).ready(function() {
	alert('ready!');
	});


</script>
	 
</head>
<body>
	<div class="maindiv1200 shadowRounded">
		<jsp:include page="banner.jsp"/>	
		<div class="contentdiv1200 shadowRounded">
		<div id="menubar"></div>
		</div>
	</div>
	change
</body>
</html>