package com.trabajo.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.annotation.ProcessState;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.engine.bobj.InstanceImpl;
import com.trabajo.engine.bobj.ProcDefs;
import com.trabajo.engine.bobj.TaskGroups;
import com.trabajo.engine.bobj.UserImpl;
import com.trabajo.engine.bobj.Users;
import com.trabajo.jpa.InstanceJPA;
import com.trabajo.jpa.ProcDefJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITaskGroup;
import com.trabajo.process.IUser;
import com.trabajo.utils.AnnotationUtils;

public class Lifecycle_START extends AbstractLifecycle  {

	private final static Logger logger = LoggerFactory.getLogger(SandBoxImpl.class);

	private Object processObject;
	private IUser initiator;

	public Lifecycle_START(LifecycleContext context) {
		super(context);
		this.initiator = Users.findByUsername(em, getRemoteUser());
		this.processObject = createProcessObject();
	}

	public void execute(Map<String, String> hParms, String note) throws ProcessException {
		IInstance instance=persistInstance(note);
		setInstance(instance);
		TaskGroups.create(em, "_default", instance);
		
		Method m = getLifeCycleMethodByPhase(processObject.getClass(), com.trabajo.annotation.StandardLifecycle.START);
		if (m == null) {
			status.error("Main process class is not annotated with @Process", logger);
			return;
		}
		
		AnnotationHandlerChain before=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_INSTANCE_START, this);
		before.setContext(this);
		before.start();
		
		Exception procEx=invokeLifeCycleMethod(processObject, m, new Object[] { hParms });

		if(procEx!=null) {
			em.remove(getInstance().entity());
			em.flush();
			status.info("Process not started: it threw an exception");
		}
		else if (getVeto() != null) {
			// TODO formalise the whole VETOING thing
			em.remove(getInstance().entity());
			em.flush();
			status.info("Process not started: veto reason: " + getVeto());
		}
		else {
			AnnotationHandlerChain after=AnnotationHandlerFactory.forPoint(ApplicationPoint.AFTER_INSTANCE_START, this);
			after.setContext(this);
			after.start();

			Field f=getStateContainerField(processObject);
			if(!f.isAccessible()) {
				f.setAccessible(true);
			}
			
			try {
				getInstance().setState((StateContainer)f.get(processObject));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			getInstance().save();
		}
	}
	
	private Field getStateContainerField(Object taskObj) {
		List<Field> fields= AnnotationUtils.getFieldsAnnotated(taskObj.getClass(), ProcessState.class);
		return fields.isEmpty() ? null: fields.get(0);
	}

	private IInstance persistInstance(String note) {
		InstanceJPA j = new InstanceJPA();
		j.setNote(note);
		j.setInitiator(((UserImpl) initiator).entity());
		StateContainer sc=new StateContainer();
		sc.put("test", "stuff");
		j.setInstanceState(new StateContainer());
		j.setProcDef((ProcDefJPA)ProcDefs.findByVersion(em, getProcessClassMetadata().getVersion()).entity());
		em.persist(j);
		em.flush();
		return new InstanceImpl(em, j);
	}

	@Override
	public Object getTaskObject() {
		throw new IllegalStateException("Action not permitted in this context");
	}

	@Override
	public Object getProcessObject() {
		return processObject;
	}

	@Override
	public Object getTaskOrProcessObject() {
		return processObject;
	}

	@Override
  public ITaskGroup createGroup(String name) {
	  return TaskGroups.create(em, name, getInstance());
  }
}