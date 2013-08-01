package com.trabajo.ejb;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.ValidationException;
import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.ClassLoaderUploadManager.ClassLoaderUpload;
import com.trabajo.engine.Engine;
import com.trabajo.engine.EngineInstance;
import com.trabajo.engine.ReportManager;
import com.trabajo.engine.SysConfig;
import com.trabajo.engine.TSession;
import com.trabajo.engine.TaskDueDate;
import com.trabajo.engine.TaskUploadManager;
import com.trabajo.engine.UserPrefs;
import com.trabajo.engine.View;
import com.trabajo.engine.bobj.Groups;
import com.trabajo.engine.bobj.Maps;
import com.trabajo.engine.bobj.Roles;
import com.trabajo.engine.bobj.Users;
import com.trabajo.process.IGroup;
import com.trabajo.process.IMap;
import com.trabajo.process.IMapLocation;
import com.trabajo.process.IRole;
import com.trabajo.process.ITaskUpload;
import com.trabajo.process.IUser;
import com.trabajo.values.CreateGroupSpec;
import com.trabajo.values.CreateRoleSpec;
import com.trabajo.values.CreateUserSpec;

/**
 * Session Bean implementation class EngineBean
 */
@Stateless
@LocalBean
public class EngineBean { 

	private final static Logger logger = LoggerFactory.getLogger(EngineBean.class);
	
	@Resource
	private TimerService timerService;

	@PersistenceContext(name = "trabajo")
	private EntityManager em;

	public EngineBean() {

	}

	private void setTimer(TaskDueDate tdd) {	
		ScheduleExpression calendarSchedule = tdd.getSchedule();
		TimerConfig timerConfig = new TimerConfig();
		tdd.persist(em);
		timerConfig.setPersistent(true);
		timerConfig.setInfo(tdd.toMap());
		Timer timer = this.timerService.createCalendarTimer(calendarSchedule, timerConfig);
		System.out.println("timeout at: "+timer.getNextTimeout());
		tdd.persist(em, timer.getHandle());
	}
	
	@SuppressWarnings("rawtypes")
	@Timeout
	private void onTimeout(Timer timer) {
		try {
			Serializable ser = timer.getInfo();
			timer.cancel();

			try{
				TaskDueDate tdd=new TaskDueDate();
				tdd.fromMap((Map)ser);
				getEngine().taskTimeout(em, tdd);
			}
			catch(IllegalArgumentException e) {
				//ignore
			}
		} catch (Exception e) {
			logger.error("error running timer", e);
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public IUser createUser(CreateUserSpec spec) {
		return (IUser) Users.createUser(em, spec);
	}

	public DefinitionVersion versionForTaskId(TSession ts, int taskId) {
		try {
			ts.setEntityManager(em);
			return getEngine().versionForTaskId(ts, taskId);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public String xml(TSession ts, String feed, FeedParms fp) {

		try {
			ts.setEntityManager(em);
			Feed f = (Feed) Class.forName("com.trabajo.feed.xml.DHXXMLFeed_" + feed).newInstance();			return f.construct(ts, getEngine(), em, fp);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalArgumentException("<error>unrecognised feed: " + feed + "</error>");
		} finally {
			ts.setEntityManager(null);
		}
	}

	public String json(TSession ts, String feed, FeedParms fp) {

		try {
			ts.setEntityManager(em);
			Feed f = (Feed) Class.forName("com.trabajo.feed.json.DHXJSONFeed_" + feed).newInstance();
			return f.construct(ts, getEngine(), em, fp);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalArgumentException("<error>unrecognised feed: " + feed + "</error>");
		} finally {
			ts.setEntityManager(null);
		}
	}

	private Engine getEngine() {
		return EngineInstance.instance;
	}

	public IUser findUser(long id) {
		return Users.findById(em, id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public IRole createRole(TSession ts, String name, String category, String desc) {
		try {
			CreateRoleSpec spec = new CreateRoleSpec(category, name, desc);
			IRole r = Roles.create(em, spec);
			return r;
		} catch (ValidationException e) {
			ts.getStatus().error("Failed to create role: " + name);
			ts.getStatus().exception("VALIDATION", e.getMessage());
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public IGroup createGroup(TSession ts, String name, String category, String desc) {
		try {
			CreateGroupSpec spec = new CreateGroupSpec(category, name, desc);
			IGroup g = Groups.create(em, spec);
			return g;
		} catch (ValidationException e) {
			ts.getStatus().error("Failed to create group: " + name);
			ts.getStatus().exception("VALIDATION", e.getMessage());
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public IGroup createGroup(String category, String name, String desc) throws ValidationException {
		CreateGroupSpec spec = new CreateGroupSpec(category, name, desc);
		return Groups.create(em, spec);
	}

	public IUser findUserByUserName(String userName) {
		return Users.findByUsername(em, userName);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deployProcess(TSession ts, File f, String originalJarame) {
		try {
			ts.setEntityManager(em);
			getEngine().deployProcess(ts, f, originalJarame, em, new SysConfig(ts));
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void processGetStartupForm(TSession ts, DefinitionVersion dv) {
		try {
			ts.setEntityManager(em);
			getEngine().processGetStartupForm(ts, dv);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void startProcess(TSession ts, DefinitionVersion dv, Map<String, String> hParms, String note) {

		try {
			ts.setEntityManager(em);
			getEngine().startProcess(ts, dv, hParms, note);

			for (TaskDueDate tdd : ts.getTaskScheduling()) {
				setTimer(tdd);
			}		

		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deployClassLoader(TSession ts, ClassLoaderUpload ul) {
		try {
			ts.setEntityManager(em);
			getEngine().deployClassLoader(ts, ul, em);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deployService(TSession ts, ClassLoaderUpload ul) {
		try {
			ts.setEntityManager(em);
			getEngine().deployService(ts, ul, em);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void selectTask(TSession ts, long taskId) {
		try {
			ts.setEntityManager(em);
			getEngine().selectTask(ts, taskId);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void claimTask(TSession ts, long taskId) {
		try {
			ts.setEntityManager(em);
			getEngine().claimTask(ts, taskId);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void disposeTask(TSession ts, long taskId, Map<String, String[]> parameterMap, String action) {

		try {
			ts.setEntityManager(em);
			getEngine().disposeTask(ts, taskId, parameterMap, action);

			for (TaskDueDate tdd : ts.getTaskScheduling()) {
				setTimer(tdd);
			}

		} finally {
			ts.setEntityManager(null);
		}
	}

	public void viewTask(TSession ts, long taskId) {
		try {
			ts.setEntityManager(em);
			getEngine().viewTask(ts, taskId);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void viewTask2(TSession ts, View view, HttpServletRequest request, HttpServletResponse response) {
		try {
			ts.setEntityManager(em);
			getEngine().viewTask2(ts, view, request, response);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createUser(TSession ts, String fullName, String email, String username, String password) {
		try {
			ts.setEntityManager(em);
			getEngine().createUser(ts, fullName, email, username, password);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void modifyRole(TSession ts, boolean set, int userid, int roleid) {
		try {
			ts.setEntityManager(em);
			getEngine().modifyRole(ts, set, userid, roleid);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void modifyGroupRoles(TSession ts, boolean set, String groupName, int roleid) {
		try {
			ts.setEntityManager(em);
			getEngine().modifyGroupRoles(ts, set, groupName, roleid);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void modifyGroupUsers(TSession ts, boolean set, String groupName, int userid) {
		try {
			ts.setEntityManager(em);
			getEngine().modifyGroupUsers(ts, set, groupName, userid);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void modifyGroup(TSession ts, boolean set, int userid, int groupid) {
		try {
			ts.setEntityManager(em);
			getEngine().modifyGroup(ts, set, userid, groupid);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void writeTaskData(TSession ts, int taskId, String name, String args, HttpServletResponse response) {
		try {
			ts.setEntityManager(em);
			getEngine().writeTaskData(ts, taskId, name, args, response);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public ITaskUpload manageUpload(TSession ts, Path target, int taskId, String originalName, String docSet, String username, long size, boolean versioned) {
		try {
			ts.setEntityManager(em);
			return TaskUploadManager.manage(ts, target, taskId, originalName, docSet, username, size, versioned);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void installReport(TSession ts, Path target, DefinitionVersion dv, String category, String description, String remoteUser, String originalName) {
		try {
			ts.setEntityManager(em);
			ReportManager.install(ts, target, dv, category, description, remoteUser, originalName);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public String getReport(TSession ts, DefinitionVersion dv) {
		try {
			ts.setEntityManager(em);
			return ReportManager.get(ts, dv);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public String getReportByName(TSession ts, String name) {
		try {
			ts.setEntityManager(em);
			return ReportManager.getReportByName(ts, name);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public SysConfig getConfig(TSession ts) {
		try {
			ts.setEntityManager(em);
			SysConfig sc = new SysConfig(ts);
			ts.getStatus().setJsonResult(sc);
			return sc;
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateConfig(TSession ts, SysConfig sc) {
		try {
			ts.setEntityManager(em);
			sc.save(ts);
		} finally {
			ts.setEntityManager(null);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void setUserPrefs(TSession ts, UserPrefs up) {
		try {
			ts.setEntityManager(em);
			up.save(ts);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void obliterate(TSession ts) {
		try {
			ts.setEntityManager(em);
			getEngine().obliterate(ts, new SysConfig(ts));
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void setProcessServiceProperty(TSession ts, DefinitionVersion pdv, DefinitionVersion sdv, String key, String value) {
		try {
			ts.setEntityManager(em);
			getEngine().setProcessServiceProperty(ts, pdv, sdv, key, value);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void getProcessGraph(TSession ts, int taskId, HttpServletResponse response) {
		SysConfig sc=getConfig(ts);
		String gvLocation=sc.getGraphVizDir();
		
		try {
			ts.setEntityManager(em);
			getEngine().writeProcessGraph(ts, gvLocation, response.getWriter(), taskId);
		} catch (IOException e) {
			ts.getStatus().error(e.getMessage(), logger, e);
		} finally {
			ts.setEntityManager(null);
		}
	}

	public void getCurrentUserMap(TSession ts, String type) {
		try {
			ts.setEntityManager(em);
			switch(type) {
			case "user_location":
				IUser currentUser=Users.findByUsername(em, ts.getRemoteUser());
				ts.getStatus().setJsonResult(Maps.findByUserAndType(em, currentUser, "user_location").toJson());
				break;
			}
		} finally {
			ts.setEntityManager(null);
		}
		
	}

	public void updateUserMapLocationMeta(TSession ts, String location, double lat, double lng, String name, String value) {
		try {
				ts.setEntityManager(em);
				IUser currentUser=Users.findByUsername(em, ts.getRemoteUser());
				IMap map=Maps.findByUserAndType(em, currentUser, "user_location");
				IMapLocation ml=map.getOrCreateLocation(location, lat, lng);
				ml.setMetaData(name, value);
				ts.getStatus().setJsonResult("{}");
		} finally {
			ts.setEntityManager(null);
		}
		
	}
}
