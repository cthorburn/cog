package com.trabajo.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.ILifecycle;
import com.trabajo.ProcessCompletion;
import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.ValidationException;
import com.trabajo.annotation.Lifecycle;
import com.trabajo.annotation.StandardLifecycle;
import com.trabajo.engine.bobj.Instances;
import com.trabajo.engine.bobj.NodeTimers;
import com.trabajo.engine.bobj.Roles;
import com.trabajo.engine.bobj.TaskBarriers;
import com.trabajo.engine.bobj.TaskGroups;
import com.trabajo.engine.bobj.Tasks;
import com.trabajo.engine.bobj.Users;
import com.trabajo.jpa.InstanceJPA;
import com.trabajo.jpa.NodeJPA;
import com.trabajo.jpa.ProcessJarAnalysis;
import com.trabajo.jpa.RoleJPA;
import com.trabajo.jpa.TaskGroupJPA;
import com.trabajo.jpa.UserJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.IRole;
import com.trabajo.process.ITask;
import com.trabajo.process.ITaskGroup;
import com.trabajo.process.IUser;
import com.trabajo.process.TaskCreator;
import com.trabajo.values.CreateRoleSpec;
import com.trabajo.vcl.ClassLoaderManager;

public  abstract class AbstractLifecycle implements ILifecycle {

	@Override
  public void setProcessDisplayString(String string) {
	  // TODO Auto-generated method stub
	  
  }

	private final static Logger logger = LoggerFactory.getLogger(AbstractLifecycle.class);

	private LifecycleContext context;

	private IInstance instance;
	private String veto;
	private Set<ITask> tasksToStart=new HashSet<>();
	private boolean prohibitCreateTask;
	protected EntityManager em;
	protected EngineStatus status;

	public AbstractLifecycle(LifecycleContext context) {
		this.context=context;
		em=context.session().getEntityManager();
		status=context.session().getStatus();
	}

	protected String getRemoteUser() {
		return context.getRemoteUser();
	}

	protected TSession getTSession() {
		return context.session();
	}

	protected ProcessClassMetadata getProcessClassMetadata() {
		return context.getPcm();
	}

	protected ProcessJarAnalysis getJarMetadata() {
		return context.getWjm();
	}

	protected ClassLoader getClassLoader() {
		return context.getClassLoader();
	}

	protected void setView(View view) {
		throw new UnsupportedOperationException();
	}

	public void ended(ITask task, StateContainer parms) {
		tasksToStart.addAll(TaskBarriers.processEndTask(em, task, parms));
	}

	@Override
	public ITask getTask() {
		throw new UnsupportedOperationException();
	}

	public String getVeto() {
		return veto;
	}
	
	@Override
	public IInstance getInstance() {
		return instance;
	}

	protected void setInstance(IInstance instance) {
		this.instance=instance;
	}

	protected Method getLifeCycleMethodByPhase(Class<?> clazz, StandardLifecycle phase) {
		for (Method m : clazz.getMethods()) {
			if (m.isAnnotationPresent(Lifecycle.class)) {
				Lifecycle l = m.getAnnotation(Lifecycle.class);
				StandardLifecycle test = l.phase();
				if (test != null && test == phase) {
					return m;
				}
			}
		}
		return null;
	}

	protected  Exception invokeLifeCycleMethod(Object obj, Method m, Object[] args) {
		Exception result=null;
		try {
			m.invoke(obj, args);
		} catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof NoSuchMethodError) {
				status.error("Required Method not present in process code: "+m, logger);
			}
			else {
				status.error(e.getTargetException().getMessage(), logger, (Exception)e.getTargetException());
			}	
			result=e;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			status.error("Failed to invoke lifecycle method: "+m.getName() + ": " + e.getMessage(), logger, e);
			result=e;
		} catch (RuntimeException e) {
			status.error("The process threw an unexpected exception: "+m.getName() + ": " + e.getMessage(), logger, e);
			result=e;
		}	
		return result;
	}
	
	protected  Exception invokeLifeCycleMethod(Object obj, Method m) {
		Exception result=null;
		try {
			m.invoke(obj);
		} catch (InvocationTargetException e) {
				status.error(e.getTargetException().getMessage(), logger, (Exception)e.getTargetException());
				result=e;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			status.error("Failed to invoke lifecycle method: "+m.getName() + ": " + e.getMessage(), logger, e);
			result=e;
		} catch (RuntimeException e) {
			status.error("The process threw an unexpected exception: "+m.getName() + ": " + e.getMessage(), logger, e);
			result=e;
		}
		
		return result;
	}
	
	protected void tryEndInstance() {
		if(allTasksComplete()) {
			try {
				new Lifecycle_END(context, getInstance()).execute();
			} catch (ProcessException e) {
				status.error("instance end called but threw exception", logger, e);
			}
			instance.end(ProcessCompletion.OK);
		}
	}
	
	private boolean allTasksComplete() {
		return Instances.allTasksComplete(em, instance);
	}

	@Override
	public Logger logger() {
		return LoggerFactory.getLogger("log_"+context.getPcm().getClassName());
	}
	
	@Override
	public void setDHXView(String formName) {
		setView(new DHXView(this, formName));
	}

	@Override
	public void setHTMLView(String processRelUrl) {
		setView(new HTMLView(this, processRelUrl));
	}

	@Override
	public void abandon(String reason) {
		this.veto=reason;
		status.error("Process not started. Reason: "+veto);
	}

	public StateContainer getInstanceState() {
		return getInstance().getState();
	}

	@Override
	public ITask createTask(String taskName, StateContainer parms) throws ProcessException {
		return createTask(new TaskCreator(taskName), parms, getInstanceGroup());
	}
	
	@Override
  public ITask createTask(String taskName, StateContainer state, ITaskGroup group) {
	  return createTask2(new TaskCreator(taskName), state, group);
	}

	@Override
  public ITask createTask(TaskCreator tc, StateContainer state, ITaskGroup group) throws ProcessException {
	  return createTask2(tc, state, group);
	}
	
	@Override
	public ITask createTask(TaskCreator tc) {
		return createTask(tc, new StateContainer(), getInstanceGroup());
	}

	@Override
	public ITask createTask(String taskName) {
		return createTask(new TaskCreator(taskName), new StateContainer());
	}
	
	@Override
  public ITask createTask(String taskName, ITaskGroup group) {
	  return createTask(taskName, new StateContainer(), group);
	}

	@Override
  public ITask createTask(TaskCreator tc, ITaskGroup group) {
	  return createTask(tc, new StateContainer(), group);
	}
	
	@Override
	public ITask createTask(TaskCreator tc, StateContainer parms) {
		return createTask(tc, parms, getInstanceGroup());
	}

	
	private ITaskGroup getInstanceGroup() {
	  return TaskGroups.getDefaultGroup(em, instance);
  }

	private ITask createTask2(TaskCreator tc, StateContainer parms, ITaskGroup group) throws ProcessException {

		if(prohibitCreateTask) {
			throw new IllegalStateException("create task not callable from this context");
		}
		Class<?> taskClass;
		try {
			String taskClassName = context.getWjm().getTaskClassName(tc.getTaskName());
			taskClass = context.getClassLoader().loadClass(taskClassName);
		} catch (ClassNotFoundException e) {
			status.error("Could not create task with name: "+tc.getTaskName());
			throw new ProcessException(e);
		}
		
		Object taskObj;
		try {
			taskObj = taskClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		IUser owner;
		
		if("system".equals(getRemoteUser())) {
			owner=getTask().getOwner();
		}
		else {
			owner=Users.findByUsername(em, getRemoteUser());
		}
		
		NodeJPA node=new NodeJPA();
		node.setInstance((InstanceJPA)getInstance().entity());
	  node.setTaskGroup((TaskGroupJPA)group.entity());

		node.setName(tc.getTaskName());
		node.setDateAssigned(new GregorianCalendar());
		node.setDateDue(new GregorianCalendar());
		node.getDateDue().add(Calendar.MINUTE, 2);
		node.setDescription("description: TODO get this from an annotation");
		node.setUserTaskedTo(null);
		
		node.setImportance("medium");
		node.setNodeState(parms==null ? new StateContainer():parms);
		node.setNodeClass(taskObj.getClass().getName());
		node.setOwner((UserJPA)owner.entity());
		em.persist(node);
		em.flush();

		Set<RoleJPA> roles=node.getRoleSet();
		for(String roleName: tc.getRoles()) {
			IRole role=Roles.findByName(em, roleName);
			if(role==null) {
				try {
					role=Roles.create(em, new CreateRoleSpec("dynamic", roleName, "no description"));
				} catch (ValidationException e) {
					status.error("Name validation for dynamic role creation failed: "+roleName);
				}
			}
			roles.add((RoleJPA)role.entity());
		}
		em.persist(node);
		
		TaskBarriers.createAll(em, node, tc.getDependencies());
		
		ITask newTask=Tasks.toImpl(em, node);
	  group.addTask(newTask);

		new Lifecycle_TASK_CREATE(context, newTask).execute();
		
		return newTask;
	}

	public Set<ITask> getTasksToStart() {
		return tasksToStart;
	}
	
	public void prevent(String string) {
		if(string.contains("createTask")) {
			prohibitCreateTask();
		}
	}

	private void prohibitCreateTask() {
		prohibitCreateTask=true;
	}
	
	//TODO SRP violation
	protected Object createProcessObject() {
		String main = context.getPcm().getClassName();
		Class<?> mainClass;
		try {
			mainClass = context.getClassLoader().loadClass(main);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			return mainClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			status.error("failed to create process object", logger, e);
			throw new RuntimeException(e);
		}		
	}
	
	//TODO SRP violation
	protected Object createTaskObject(ITask node) {
		String task = node.getNodeClassName();
		try {
			Class<?> taskClass = getClassLoaderManager().loadClass(task);
			return taskClass.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | RepositoryException e) {
			status.error("Failed to create task object", logger, e);
			throw new RuntimeException(e);
		}
	}

	public void cancelTimer(String name) {
		INodeTimer nodeTimer=NodeTimers.findByNameTask(em, name, getTask());
		if(nodeTimer!=null) {
			nodeTimer.cancel();
		}
	}

	public EntityManager getEntityManager() {
		return context.session().getEntityManager();
	}
	public TSession session() {
		return context.session();
	}

	public abstract Object getTaskObject();
	public abstract Object getProcessObject();

	public ClassLoaderManager<DefinitionVersion, DVFactory> getClassLoaderManager() {
		return context.getClassLoaderManager();
	}

	
	@Override
  public IUser getProcessInitiator() {
	  
	  return instance.getInitiator();
  }

	public ProcessRegistry getProcessRegistry() {
		return context.getProcessRegistry();
	}

	public abstract Object getTaskOrProcessObject();
}
