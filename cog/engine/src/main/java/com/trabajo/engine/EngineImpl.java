package com.trabajo.engine;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.IProcessServiceProperties;
import com.trabajo.IVisualizer;
import com.trabajo.ValidationException;
import com.trabajo.engine.bobj.ClassLoaderDefs;
import com.trabajo.engine.bobj.Groups;
import com.trabajo.engine.bobj.ProcDefImpl;
import com.trabajo.engine.bobj.ProcDefs;
import com.trabajo.engine.bobj.Roles;
import com.trabajo.engine.bobj.Tasks;
import com.trabajo.engine.bobj.Users;
import com.trabajo.jpa.ClassLoaderDefJPA;
import com.trabajo.jpa.ProcDefJPA;
import com.trabajo.jpa.ServiceDefJPA;
import com.trabajo.jpa.VersionJPA;
import com.trabajo.jpa.WholeJarMetadataV1;
import com.trabajo.process.IGroup;
import com.trabajo.process.IInstance;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.IProcDef;
import com.trabajo.process.IRole;
import com.trabajo.process.ITask;
import com.trabajo.process.IUser;
import com.trabajo.values.CreateUserSpec;

@SubsystemMetadata(mbeanInterfaceClass = EngineImplMBean.class, objectName = "com.trabajo.engine:type=Engine")
public class EngineImpl implements Engine, EngineImplMBean {

	private ProcessRegistry pr;
	private ContextFactory cf;

	private Logger logger = LoggerFactory.getLogger(EngineImpl.class);

	public EngineImpl(ProcessRegistry pr, ContextFactory cf) {
		this.pr = pr;
		this.cf = cf;
		registerURLHandler();

	}

	private void registerURLHandler() {
		System.setProperty("java.protocol.handler.pkgs", "com.trabajo.engine.urlhandler.trabajo");
	}

	public String getName() {
		return "Engine";
	}

	@Override
	public void deployProcess(TSession ts, File f, String originalJarName, EntityManager em, SysConfig sc) {
		EngineStatus status = ts.getStatus();

		try {
			ProcessJar pj = null;
			try {
				pj = new ProcessJar(f);
			} catch (ValidationException e) {
				status.error(e.getMessage(), logger, e);
				return;
			}

			ProcessClassMetadata pcm = pj.getClassMetadata();
			WholeJarMetadataV1 wjm;
			try {
				wjm = new WholeJarMetadataV1(f);
			} catch (ValidationException e) {
				status.error(e.getMessage(), logger, e);
				return;
			}
	

			DefinitionVersion dv=pcm.getVersion();
			if(pr.processExists(dv)) {
				if(pcm.inDevelopment() && pcm.removePreviousVersions()) {
					  cf.obliterate(dv);
						IProcDef pdi = ProcDefs.findByVersion(em, pcm.getVersion());
						pdi.obliterate();
						pr.obliterate(em, dv, sc);
				}
				else {
					status.exception("DUPLICATE_PROCESS_ID", "A process with this name already exists and this process is not annotated @Development");
					return;
				}
			}
				
			try {
				pr.deployProcess(pj, originalJarName);
			} catch (ValidationException e) {
				status.error(e.getMessage(), logger, e);
				return;
			}
			
			IProcDef pdi = ProcDefs.findByVersion(em, pcm.getVersion());

			if (pdi == null) {
				ProcDefJPA pd = new ProcDefJPA();
				pd.setDefinitionVersion(new VersionJPA(pcm.getVersion()));
				pdi=new ProcDefImpl(em, pd);
			}
			pdi.setCategory(pcm.getCategory().getValue());
			pdi.setMainClass(pcm.getClassName());
			pdi.setDescription(pcm.getDescription().getValue());
			((ProcDefJPA)pdi.entity()).setJarMetadata(wjm);

			em.persist(pdi.entity());

			try {
				em.flush();
			} catch (EntityExistsException e) {
				status.warning("Entity already exists", logger, e);
			}
		} catch (RuntimeException | RepositoryException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		} 
	}

	@Override
	public void deployClassLoader(TSession ts, ClassLoaderUploadManager.ClassLoaderUpload clu, EntityManager em) {
		EngineStatus status = ts.getStatus();
		if(clu==null) {
			status.error("No class loader upload context", logger);
			return;
		}

		try {
			pr.deployClassLoader(clu);

			ClassLoaderDefJPA pd = ClassLoaderDefs.findByVersion(em, clu.version);

			if (pd == null) {
				pd = new ClassLoaderDefJPA();
				pd.setDefinitionVersion(new VersionJPA(clu.version));
			}

			pd.setCategory(clu.category.getValue());
			pd.setDescription(clu.desc.getValue());
			em.persist(pd);
			try {
				em.flush();
			} catch (EntityExistsException e) {
				status.warning("Entity already exists", logger, e);
			}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	

	@Override
	public void deployService(TSession ts, ClassLoaderUploadManager.ClassLoaderUpload clu, EntityManager em) {
		EngineStatus status = ts.getStatus();

		try {
			pr.deployClassLoader(clu);
			ServiceDefJPA pd = ServiceDefs.findByVersion(em, clu.version);
			if (pd == null) {
				pd = new ServiceDefJPA();
				pd.setDefinitionVersion(new VersionJPA(clu.version));
			}
			pd.setCategory(clu.category.getValue());
			pd.setDescription(clu.desc.getValue());
			em.persist(pd);

			try {
				em.flush();
			} catch (EntityExistsException e) {
				status.warning("Entity already exists", logger, e);
			}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void processGetStartupForm(TSession ts, DefinitionVersion dv) {
		EngineStatus status = ts.getStatus();

		try {
			try {
				status.setResult(pr.getStore().createProcessJar(dv).getClassMetadata().getStartupForm());
			} catch (RepositoryException e) {
				ts.getStatus().exception("REPOSITORY", e.getMessage(), logger, e);
			} catch (IOException e) {
				ts.getStatus().exception("IO", e.getMessage(), logger, e);
			}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void startProcess(TSession ts, DefinitionVersion dv, Map<String, String> hParms, String note) {
		EngineStatus status = ts.getStatus();

		try {
			try {
				cf.newSandbox(ts, pr.getProcessClassMetadata(dv), getJarMetadata(ts, dv)).start(hParms, note);
			} catch (RepositoryException e) {
				ts.getStatus().exception("REPOSITORY", e.getMessage(), logger, e);
			} catch (IOException e) {
				ts.getStatus().exception("IO", e.getMessage(), logger, e);
			}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	private WholeJarMetadataV1 getJarMetadata(TSession ts, DefinitionVersion dv) {
		return ((ProcDefJPA)ProcDefs.findByVersion(ts.getEntityManager(), dv).entity()).getJarMetadata();
	}

	@Override
	public void selectTask(TSession ts, long taskId) {
		EngineStatus status = ts.getStatus();

		try {
			ITask node = Tasks.findById(ts.getEntityManager(), taskId);
			DefinitionVersion dv = node.getInstance().getProcDef().getDefinitionVersion();

			try {
				cf.newSandbox(ts, pr.getProcessClassMetadata(dv), getJarMetadata(ts, dv)).selectTask(node);
			} catch (RepositoryException e) {
				ts.getStatus().exception("REPOSITORY", e.getMessage(), logger, e);
			} catch (IOException e) {
				ts.getStatus().exception("IO", e.getMessage(), logger, e);
			}

			EntityManager em = ts.getEntityManager();
			em.persist(node.entity());
			em.flush();
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void claimTask(TSession ts, long taskId) {
		EngineStatus status = ts.getStatus();

		try {
			ITask node = Tasks.findById(ts.getEntityManager(), taskId);
			node.assignToUser(Users.findByUsername(ts.getEntityManager(), ts.getRemoteUser()));
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void viewTask(TSession ts, long taskId) {
		EngineStatus status = ts.getStatus();

		try {
			ITask node = Tasks.findById(ts.getEntityManager(), taskId);
			DefinitionVersion dv = node.getInstance().getProcDef().getDefinitionVersion();

			try {
				cf.newSandbox(ts, pr.getProcessClassMetadata(dv), getJarMetadata(ts, dv)).viewTask(node);
			} catch (RepositoryException | IOException e) {
				ts.getStatus().error("Could not access process", logger, e);
			}

			EntityManager em = ts.getEntityManager();
			em.persist(node.entity());
			em.flush();
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void viewTask2(TSession ts, View view, HttpServletRequest request, HttpServletResponse response) {

		EngineStatus status = ts.getStatus();

		try {
			ITask node = Tasks.findById(ts.getEntityManager(), view.getTaskId());

			try {
				cf.newSandbox(ts, pr.getProcessClassMetadata(view.getVersion()), getJarMetadata(ts, view.getVersion())).viewTask2(node, view, request, response);
			} catch (RepositoryException e) {
				ts.getStatus().exception("REPOSITORY", e.getMessage(), logger, e);
			} catch (IOException e) {
				ts.getStatus().exception("IO", e.getMessage(), logger, e);
			}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void disposeTask(TSession ts, long taskId, Map<String, String[]> parameterMap, String action) {
		EngineStatus status = ts.getStatus();

		try {
			ITask  node = Tasks.findById(ts.getEntityManager(), taskId);
			DefinitionVersion dv = node.getInstance().getProcDef().getDefinitionVersion();

			try {
				cf.newSandbox(ts, pr.getProcessClassMetadata(dv), getJarMetadata(ts, dv)).disposeTask(node, parameterMap, action);
			} catch (RepositoryException | IOException e) {
				ts.getStatus().error("Could not access task", logger, e);
			}
			status.info("Task Disposed Successfully");
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void createUser(TSession ts, String fullName, String email, String username, String password) {
		EngineStatus status = ts.getStatus();

		try {
			try {
				Users.createUser(ts.getEntityManager(), new CreateUserSpec("", fullName, username, password, email, "EN", "EN"));
			} catch (ValidationException e) {
				ts.getStatus().exception("VALIDATION", e.getMessage(), logger, e);
			}
		} catch (EntityExistsException e) {
			status.exception("DUPLICATE_USERNAME", "Username must be unique within the system.");
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public ClassLoaderDescriptor getClassLoaderDescriptor(DefinitionVersion version) {
		return pr.getClassLoaderDescriptor(version);
	}

	@Override
	public void modifyRole(TSession ts, boolean set, int userid, int roleid) {
		EngineStatus status = ts.getStatus();
		EntityManager em=ts.getEntityManager();
		
		IRole r=Roles.findById(em, roleid);
		IUser u=Users.findById(em, userid);
		
		try {
				if(set) {
					u.addRole(r);
					status.info("Role added: "+r.getName());
				}
				else {
					u.removeRole(r);
					status.info("Role removed: "+r.getName());
				}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void modifyGroupRoles(TSession ts, boolean set, String groupName, int roleid) {
		EngineStatus status = ts.getStatus();
		EntityManager em=ts.getEntityManager();
		
		IGroup g=Groups.findByName(em, groupName);
		IRole r=Roles.findById(em, roleid);
		
		try {
				if(set) {
					g.add(r);
					status.info("Role added: "+r.getName());
				}
				else {
					g.remove(r);
					status.info("Role removed: "+r.getName());
				}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void modifyGroup(TSession ts, boolean set, int userid, int groupid) {
		EngineStatus status = ts.getStatus();
		EntityManager em=ts.getEntityManager();
		
		IGroup g=Groups.findById(em, groupid);
		IUser u=Users.findById(em, userid);
		try {
				if(set) {
					g.add(u);
					status.info("User added to group: "+g.getName());
				}
				else {
					g.remove(u);
					status.info("User removed from group: "+g.getName());
				}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}
	
	@Override
	public void modifyGroupUsers(TSession ts, boolean set, String groupName, int userid) {
		EngineStatus status = ts.getStatus();
		EntityManager em=ts.getEntityManager();
		
		IGroup g=Groups.findByName(em, groupName);
		IUser u=Users.findById(em, userid);
		try {
				if(set) {
					g.add(u);
					status.info("User added to group: "+g.getName());
				}
				else {
					g.remove(u);
					status.info("User removed from group: "+g.getName());
				}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public FileCache getCache() {
		return pr.getCache();
	}

	@Override
	public DefinitionVersion versionForTaskId(TSession ts, int taskId) {
			ITask iTask=Tasks.findById(ts.getEntityManager(), taskId);
			IInstance instance=iTask.getInstance();
			IProcDef procDef=instance.getProcDef();
			return procDef.getDefinitionVersion();
	}

	@Override
	public void writeTaskData(TSession ts, int taskId, String name, String args, HttpServletResponse response) {
		EngineStatus status = ts.getStatus();

		try {
			ITask  node = Tasks.findById(ts.getEntityManager(), taskId);
			DefinitionVersion dv = node.getInstance().getProcDef().getDefinitionVersion();

			try {
				cf.newSandbox(ts, pr.getProcessClassMetadata(dv), getJarMetadata(ts, dv)).writeTaskData(node, name, args, response);
			} catch (RepositoryException | IOException e) {
				ts.getStatus().error("Could not access task for data: "+name, logger, e);
			}
		} catch (RuntimeException ee) {
			status.exception("RUNTIME", ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void taskTimeout(EntityManager em, TaskDueDate taskDueDate) {
		INodeTimer nodeTimer=taskDueDate.getNodeTimer(em);
		
		if(nodeTimer==null)
			return;
		
		ITask timeoutTask=nodeTimer.getTask();
		
		TSession ts=new TSession();
		ts.newRequest();
		ts.setRemoteUser("system");
		ts.setEntityManager(em);
		
		try {
			DefinitionVersion dv = timeoutTask.getInstance().getProcDef().getDefinitionVersion();

			try {
				cf.newSandbox(ts, pr.getProcessClassMetadata(dv), getJarMetadata(ts, dv)).taskTimeout(nodeTimer);
			} catch (RepositoryException | IOException e) {
				ts.getStatus().error("Could not process timeout", logger, e);
			}
		} catch (RuntimeException ee) {
			ts.getStatus().error(ee.getMessage(), logger, ee);
		}
	}

	@Override
	public void obliterate(TSession ts, SysConfig sc) {
		pr.obliterate(ts.getEntityManager(), sc);
	}

	@Override
	public void systemTimeout(EntityManager em) {
		System.out.println("5 minute timer!");
		
	}

	@Override
	public IProcessServiceProperties getProcessServiceProperties(TSession ts, DefinitionVersion dv) {
			try {
				return cf.newSandbox(ts, pr.getProcessClassMetadata(dv), getJarMetadata(ts, dv)).getProcessServiceProperties();
			} catch (RepositoryException | IOException e) {
				throw new RuntimeException(e);
			}
	}

	@Override
	public void setProcessServiceProperty(TSession ts, DefinitionVersion processDv, DefinitionVersion serviceDv, String key, String value) {
		try {
			pr.setProcessServiceProperty(processDv, serviceDv, key, value);
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeProcessGraph(TSession ts, String gvLocation, Writer w,  int taskId) {
		
		EntityManager em=ts.getEntityManager();
		ITask task=Tasks.findById(em, taskId);
		IInstance instance=task.getInstance();
		IVisualizer viz=instance.getVisualizer();
		try {
			
			GraphViz.generate(gvLocation, w, viz.generate(new GraphEntityFinderImpl(ts.getEntityManager())));
		} catch (IOException | TransformerException e) {
			ts.getStatus().error(e.getMessage(), logger, e);
		}
		
	}
}
