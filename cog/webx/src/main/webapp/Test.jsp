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
<link rel="stylesheet" href="js/dhx/dhtmlxForm/codebase/skins/dhtmlxform_dhx_skyblue.css"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script src="js/dhx/dhtmlxForm/codebase/dhtmlxform.js"></script>

<script>
$(document).ready(function() {
	debugger;
	
	var formStructure = [
	                 {type:"settings",position:"label-top"},
	                 {type: "fieldset",name:"calculator", label: "Calculator", list:[
	                     {type: "input", name: 'firstNum', label: 'First number:'},
	                     {type:"input", name:"secNum", label:"Second number:"},
	                     {type:"input", name:"resNum", label:"Result:"},
	                     {type:"newcolumn"},
	                     {type:"button", name:"plus", width:20,offsetTop:2, value:"+"}, 
	                     {type:"button", name:"minus",width:20,offsetTop:10, value:"-"},
	                     {type:"button", name:"multiply",width:20,offsetTop:10, value:"*"},
	                     {type:"button", name:"divide",width:20,offsetTop:10, value:"/"}
	                 ]}
	             ];
	var xml='<items>'
	+'<item type="fieldset" name="data" label="Welcome" inputWidth="auto">'
	+'<item type="input" name="name" label="Login" position="label-top"/>'
	+'<item type="button" name="save" value="Proceed"/>'
	+'</item>'
+'</items>';


//	form.loadStruct('NewFile.jsp');
	var form=new dhtmlXForm('Admin_userEdit_div', formStructure);

});
</script>
</head>
<body>
<div id="Admin_userEdit_div" style="border: 1px solid black; margin-left:10px; float: left; width:200px; height:300px;"></div>
</body>
</html>