package com.trabajo.engine;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.ParameterAdapter;
import com.trabajo.ProcessException;
import com.trabajo.RawParameterValue;
import com.trabajo.TaskCompletion;
import com.trabajo.annotation.ActionParameters;
import com.trabajo.annotation.Data;
import com.trabajo.annotation.StandardTaskLifecycle;
import com.trabajo.engine.annotation.ApplicationPoint;
import com.trabajo.process.ITask;
import com.trabajo.utils.AnnotationUtils;

public class Lifecycle_TASK_DISPOSE extends AbstractLifecycle {
	
	private final static Logger logger = LoggerFactory.getLogger(Lifecycle_TASK_DISPOSE.class);

	private Object taskObject;
	private ITask node;

	public Lifecycle_TASK_DISPOSE(LifecycleContext context, ITask node) {
		super(context);
		setInstance(node.getInstance());
		this.node=node;
		taskObject=createTaskObject(node);
	}

	public void execute(Map<String, String[]> tParms, String action) throws ProcessException  {
		if(node.getComplete()==TaskCompletion.OK) {
			status.info("This task is marked completed OK");
			return;
		}
		
		try {
			injectActionParameters(action, tParms);
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			status.error("failed to inject parameter manager for action: "+action, logger, e);
			throw new RuntimeException(e);
		}
		
		AnnotationHandlerChain before=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_TASK_DISPOSE, this);
		before.setContext(this);
		before.start();

		new TaskPhaseRunner(taskObject, StandardTaskLifecycle.ACTION, session(), null, node).run(action);
		node.save();
		
		AnnotationHandlerChain after=AnnotationHandlerFactory.forPoint(ApplicationPoint.AFTER_TASK_DISPOSE, this);
		after.setContext(this);
		after.start();

		node.getInstance().save();
		
		tryEndInstance(); 		//TODO instance end ah
	}
	
	private void injectActionParameters(String action, Map<String, String[]> tParms) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {
		List<Field> apfs=AnnotationUtils.getFieldsAnnotated(taskObject.getClass(), ActionParameters.class);

		outer: for (Field f: apfs) {
			ActionParameters ap=f.getAnnotation(ActionParameters.class);
			
			for(String s: ap.actions()) {
				if(action.equals(s)) {
					injectActionParameters2(f, ap.adapter(), tParms, action);
				}
				break outer;
			}
		}
	}

	private void injectActionParameters2(Field f, Class<?> adapter, Map<String, String[]> tParms, String action) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(!f.isAccessible()) {
			f.setAccessible(true);
		}
		
		Map<String, RawParameterValue> adjusted=new HashMap<>();
		
		for(Map.Entry<String, String[]> entry: tParms.entrySet()) {
			adjusted.put(entry.getKey(), new 	RawParameterValueImpl(entry.getKey(), entry.getValue()));
		}
		
		ParameterAdapter<?> pa=(ParameterAdapter<?>)adapter.newInstance();
		f.set(taskObject, pa);
		
		adapter.getMethod("adapt", Map.class, String.class).invoke(pa, adjusted, action);
	}

	@Override
	public void setView(View view) {
		throw new RuntimeException("setView not allowed in this context");
	}


	public void writeData(String name, String args, HttpServletResponse response) {

		Method[] methods=taskObject.getClass().getMethods();
		
		for(Method method: methods) {
			if(method.isAnnotationPresent(Data.class)) {
				Data data=method.getAnnotation(Data.class);
				if(name.equals(data.name())) {
					try {
						boolean json = false;
						
						//TODO proxy writer to prevent close doing anything
						String ctype=data.contentType();
						if(ctype.toLowerCase().contains("json")) {
							json = true;
						}
						response.setContentType(ctype);
						Writer w=response.getWriter();

						if(json) {
							w.write("{ \"ok\": true, \"jsonResult\": ");
						}
						
						AnnotationHandlerChain before=AnnotationHandlerFactory.forPoint(ApplicationPoint.BEFORE_TASK_DATA, this);
						before.setContext(this);
						before.start();
						
						method.invoke(taskObject, new Object[] {args, w});
						
						AnnotationHandlerChain after=AnnotationHandlerFactory.forPoint(ApplicationPoint.AFTER_TASK_DATA, this);
						after.setContext(this);
						after.start();
						
						if(json) {
							w.write("}");
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						status.error("task data write failed, could not invoke method for: "+name+" "+e.getMessage(), logger, e);
						throw new RuntimeException(e);
					} catch (IOException e) {
						status.error("task data write failed, could not get writer implementation for: "+name+" "+e.getMessage(), logger, e);
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	@Override
	public ITask getTask() {
		node.setLifecycle(this);
		return node;
	}

	@Override
	public Object getTaskObject() {
		return taskObject;
	}

	@Override
	public Object getProcessObject() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getTaskOrProcessObject() {
		return taskObject;
	}
}
