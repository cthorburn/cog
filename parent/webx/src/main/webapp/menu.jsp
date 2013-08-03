<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en"/>
<fmt:setBundle basename="com.trabajo.i18n.Messages" var="lang"/>
<menu>
	<c:if test="">
		<item id="users" text="<fmt:message bundle="${lang}"  key="Users"/>"> 
			<item id="admin:users_new" text="<fmt:message bundle="${lang}"  key="New"/>" /> 
			<item id="admin:users_browse" text="<fmt:message bundle="${lang}"  key="Browse"/>" /> 
			<item id="admin:users_find" text="<fmt:message bundle="${lang}"  key="Find"/>" /> 
		</item> 
	</c:if>
	<item id="roles" text="<fmt:message bundle="${lang}"  key="Roles"/>"> 
		<item id="admin:roles_new" text="<fmt:message bundle="${lang}"  key="New"/>" /> 
		<item id="admin:roles_browse" text="<fmt:message bundle="${lang}"  key="Browse"/>" /> 
		<item id="admin:roles_find" text="<fmt:message bundle="${lang}"  key="Find"/>" /> 
		<item id="admin:roles_assign_user" text="<fmt:message bundle="${lang}"  key="Assign_Users"/>"/> 
		<item id="admin:roles_assign_permissions" text="<fmt:message bundle="${lang}"  key="Assign_Permissions"/>"/> 
	</item> 
	<item id="processes" text="<fmt:message bundle="${lang}"  key="Processes"/>"> 
		<item id="admin:processes_new" text="<fmt:message bundle="${lang}"  key="New"/>" /> 
		<item id="admin:processes_browse" text="<fmt:message bundle="${lang}"  key="Browse"/>" /> 
		<item id="admin:processes_find" text="<fmt:message bundle="${lang}"  key="Find"/>" /> 
	</item> 
	<item id="permissions" text="<fmt:message bundle="${lang}"  key="Permissions"/>"> 
		<item id="admin:permissions_new" text="<fmt:message bundle="${lang}"  key="New"/>" /> 
		<item id="admin:permissions_browse" text="<fmt:message bundle="${lang}"  key="Browse"/>" /> 
	</item> 
	<item id="server" text="<fmt:message bundle="${lang}"  key="Server"/>"> 
		<item id="admin:server_configure" text="<fmt:message bundle="${lang}"  key="Configure"/>" /> 
	</item> 
</menu>
