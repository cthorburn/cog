package com.trabajo.engine;

import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;

import com.trabajo.engine.bobj.Users;



public class TSession implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean dbInitDone;
	
	private ClassLoaderUploadManager clum;
	private String remoteUser;
	private List<String> userRoles;

	private transient ThreadLocal<EntityManager> em; 
	private transient ThreadLocal<EngineStatus> status;

	public TSession() {
		clum=new ClassLoaderUploadManager();
	}

	public TSession(String remoteUser) {
		this();
		this.remoteUser=remoteUser;
	}

	public ClassLoaderUploadManager getClassLoaderUploadManager() {
		return clum;
	}

	public String getRemoteUser() {
		return remoteUser;
	}

	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}

	public void setEntityManager(EntityManager em) {
		this.em.set(em);
		if(em!=null && !dbInitDone)
			userRoles=Users.getUserRoleNames(em, remoteUser);
	}
	
	public EntityManager getEntityManager() {
		return em.get();
	}

	public EngineStatus  getStatus() {
		return this.status.get();

	}

	public TimeZone getUserTimeZone() {
		//TODO contextualise by user
		return TimeZone.getDefault();
	}

	public void newRequest() {
		if(status==null) { 
			status=new ThreadLocal<EngineStatus>(); 
		}
		status.set(new EngineStatus());
		
		if(em==null) { 
			em=new ThreadLocal<EntityManager>();
		}	
	}

	public boolean userHasRole(String role) {
		return userRoles.contains(role);
	}

	public List<TaskDueDate> getTaskScheduling() {
		return getStatus().getTaskScheduling();
	}
}
