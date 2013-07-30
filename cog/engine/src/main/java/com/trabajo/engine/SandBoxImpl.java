package com.trabajo.engine;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.IProcessServiceProperties;
import com.trabajo.ProcessException;
import com.trabajo.annotation.Service;
import com.trabajo.jpa.WholeJarMetadataV1;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.ITask;
import com.trabajo.utils.FieldVisitor;
import com.trabajo.utils.Visitation;
import com.trabajo.vcl.ClassLoaderManager;
import com.trabajo.vcl.NoSuchResourceException;
import com.trabajo.vcl.ResourceUnavailableException;
import com.trabajo.vcl.VCLThreadContext;

public class SandBoxImpl extends Observable implements Sandbox {

	private final static Logger logger = LoggerFactory.getLogger(SandBoxImpl.class);

	private ClassLoaderManager<DefinitionVersion, DVFactory> clm;
	private TSession ts;
	private VCLThreadContext<DefinitionVersion> tc;
	private EngineStatus status;
	private LifecycleContext context;

	public SandBoxImpl(TSession ts, ClassLoaderManager<DefinitionVersion, DVFactory> clm) {
		this.ts = ts;
		this.clm = clm;
		status = ts.getStatus();
	}

	@Override
	public void setMetadata(ProcessClassMetadata pcm, WholeJarMetadataV1 wjm) {
		try {
			tc = clm.setProcessContext(pcm.getVersion(), pcm.getDefaultClassLoaderHierarchy());
		} catch (NoSuchResourceException | ResourceUnavailableException | RepositoryException e) {
			status.error("could not create classloader context: ", logger, e);
			return;
		}
		try {
			this.context = new LifecycleContext(ts, tc.getChain().getFirst(), clm, pcm, wjm);
		} catch (RepositoryException e) {
			status.error("The process failed internally: " + e.getMessage(), logger, e);
		}
	}

	@Override
	public void start(Map<String, String> hParms, String note) {
		try {
			Lifecycle_START ls = new Lifecycle_START(context);
			ls.execute(hParms, note);
			startTasks(tc, clm, ls.getTasksToStart());
		} catch (ProcessException e) {
			status.error("The process detected a problem with its dependencies: " + e.getMessage(), logger, e);
		} catch (RuntimeException e) {
			status.error("The process failed internally: " + e.getMessage(), logger, e);
		} finally {
			clm.clearProcessContext();
		}
	}

	@Override
	public void selectTask(ITask node) {
		try {
			String task = node.getNodeClassName();
			Class<?> taskClass = clm.loadClass(task);
			status.setJsonResult(new TaskSummary(context.getPcm(), context.getWjm(), ts, taskClass));
		} catch (ClassNotFoundException e) {
			status.error("Did not create task: " + e.getMessage(), logger, e);
		} catch (RepositoryException e) {
			status.error("The task failed internally: " + e.getMessage(), logger, e);
		} catch (RuntimeException e) {
			status.error("The process failed internally: " + e.getMessage(), logger, e);
		} finally {
			clm.clearProcessContext();
		}
	}

	@Override
	public void viewTask2(ITask node, View view, HttpServletRequest request, HttpServletResponse response) {
		try {
			Lifecycle_TASK_VIEW ls = new Lifecycle_TASK_VIEW(context, node);
			ls.dispose(view, request, response);
			startTasks(tc, clm, ls.getTasksToStart());
		} catch (RuntimeException e) {
			status.error("The process failed internally: " + e.getMessage(), logger, e);
		} finally {
			clm.clearProcessContext();
		}
	}

	@Override
	public void disposeTask(ITask node, Map<String, String[]> parameterMap, String action) {
		try {
			Lifecycle_TASK_DISPOSE ls = new Lifecycle_TASK_DISPOSE(context, node);
			ls.execute(parameterMap, action);
			startTasks(tc, clm, ls.getTasksToStart());
		} catch (ProcessException e) {
			status.error("The task failed internally: " + e.getMessage(), logger, e);
		} catch (RuntimeException e) {
			status.error("The process failed internally: " + e.getMessage(), logger, e);
		} finally {
			clm.clearProcessContext();
		}
	}

	@Override
	public void viewTask(ITask node) {
		try {
			Lifecycle_TASK_VIEW ls = new Lifecycle_TASK_VIEW(context, node);
			ls.prepareView();
		} finally {
			clm.clearProcessContext();
		}
	}

	@Override
	public void writeTaskData(ITask node, String name, String args, HttpServletResponse response) {
		try {
			Lifecycle_TASK_DISPOSE ls = new Lifecycle_TASK_DISPOSE(context, node);
			ls.prevent("createTask"); // @Data annotated methods may not call
																// createTask()
			ls.writeData(name, args, response);
		} finally {
			clm.clearProcessContext();
		}
	}

	private void startTasks(VCLThreadContext<DefinitionVersion> tc, ClassLoaderManager<?, ?> clm, Set<ITask> tasksToStart) {

		Set<ITask> tasks = tasksToStart;
		Set<ITask> moreTasks = new HashSet<>();

		while (!tasks.isEmpty()) {

			for (Iterator<ITask> itr = tasks.iterator(); itr.hasNext();) {
				ITask task = itr.next();
				itr.remove();
				Lifecycle_TASK_CREATE ls;
				try {
					ls = new Lifecycle_TASK_CREATE(context, task);
					ls.execute();
					moreTasks.addAll(ls.getTasksToStart());
				} catch (ProcessException e) {
					status.error("Task CREATE threw exception", logger, e);
				} catch (RuntimeException e) {
					status.error("Task CREATE threw exception", logger, e);
				}
			}
			tasks.addAll(moreTasks);
			moreTasks.clear();
		}
	}

	@Override
	public void taskTimeout(INodeTimer nodeTimer) {
		try {
			ITask task = nodeTimer.getTask();
			Lifecycle_TIMEOUT ls = new Lifecycle_TIMEOUT(context, task, nodeTimer);
			ls.doTimeout();
		} catch (ProcessException e) {
			status.error("Task CREATE threw exception", logger, e);
		} finally {
			clm.clearProcessContext();
		}
	}

	@Override
	public IProcessServiceProperties getProcessServiceProperties() throws RepositoryException, IOException {
		try {
			IProcessServiceProperties psp;
			DefinitionVersion dv = tc.getChain().getClmKey();

			ProcessRegistry pr=(ProcessRegistry)clm.getChainProvider();
			psp = pr.getProcessServiceProperties(dv);
			

			if (psp == null) {
				String main = context.getPcm().getClassName();
				Class<?> mainClass;
				try {
					mainClass = context.getClassLoader().loadClass(main);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}

				List<DefinitionVersion> processServices = searchServices(mainClass, tc.classLoaderNames());
				psp = new ProcessServiceProperties(dv);

				for (DefinitionVersion psDv : processServices) {
					Properties p = ((ProcessRegistry) clm.getChainProvider()).getServiceProperties(psDv);
					psp.addServiceProperties(psDv, p);
				}
				pr.setProcessServiceProperties(psp);
			}

			return psp;
		} finally {
			clm.clearProcessContext();
		}
	}

	private List<DefinitionVersion> searchServices(final Class<?> processClass, final List<DefinitionVersion> clh) {
		final List<DefinitionVersion> processServices = new ArrayList<>();

		Visitation<Field> v = new Visitation<Field>() {
			@Override
			public void visit(Field f) {
				Service svcAnn = f.getAnnotation(Service.class);
				if (svcAnn != null) {
					String svcName= svcAnn.name();
					String svcVersion = svcAnn.version();
					StringBuilder sb=new StringBuilder();
					sb.append(svcName);
					sb.append('-');
					sb.append(svcVersion);
					try {
						processServices.add(DefinitionVersion.parse(sb.toString()));
					} catch (Exception e) {
						// service def is in versionless format
						// look for the classloader with the same prefix

						for (DefinitionVersion clDv : clh) {
							if (clDv.shortName().equals(svcName)) {
								processServices.add(clDv);
							}
						}
					}
				}
			}
		};
		new FieldVisitor(processClass, v);

		return processServices;
	}

}
