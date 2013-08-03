package com.trabajo.engine;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.DefinitionVersion;
import com.trabajo.IProcessServiceProperties;

public interface Engine {

		//deployment
		void deployProcess(TSession tsession, File f, String originalJarName, EntityManager em, SysConfig sc);
		void deployService(TSession tsession, ClassLoaderUploadManager.ClassLoaderUpload clu, EntityManager em);
		void deployClassLoader(TSession tsession, ClassLoaderUploadManager.ClassLoaderUpload clu, EntityManager em);
		void deprecateProcess(TSession ts, DefinitionVersion dv, boolean deprecate);
		void suspendProcess(TSession ts, DefinitionVersion dv, boolean suspend);
		void purgeProcess(TSession ts, DefinitionVersion dv);
    
		//users
		void createUser(TSession ts, String fullName, String email, String username, String password);
    
		//tasks
		void claimTask(TSession tsession, long taskId);
		void selectTask(TSession session, long taskId);
		void viewTask(TSession tsession, long taskId);
		void viewTask2(TSession ts, View view, HttpServletRequest request, HttpServletResponse response);
		void disposeTask(TSession ts, long taskId, Map<String, String[]> parameterMap, String action);
		void processGetStartupForm(TSession session, DefinitionVersion dv);
		void startProcess(TSession session, DefinitionVersion dv, Map<String, String> hParms, String note) ;
		ClassLoaderDescriptor getClassLoaderDescriptor(DefinitionVersion version);
		void modifyRole(TSession ts, boolean set, int userid, int name);
		void modifyGroup(TSession ts, boolean set, int userid, int name);
		FileCache getCache();
		DefinitionVersion versionForTaskId(TSession ts, int taskId);
		void writeTaskData(TSession ts, int taskId, String name, String args, HttpServletResponse response);
		void taskTimeout(EntityManager em, TaskDueDate taskDueDate);
		void modifyGroupRoles(TSession ts, boolean set, String groupName, int roleid);
		void modifyGroupUsers(TSession ts, boolean set, String groupName, int userid);
		void systemTimeout(EntityManager em);
		IProcessServiceProperties getProcessServiceProperties(TSession ts, DefinitionVersion dv);
		void setProcessServiceProperty(TSession ts, DefinitionVersion processDv, DefinitionVersion serviceDv, String key, String value);
		void writeProcessGraph(TSession ts, String gvLocation, Writer w, int taskId);
		
}
