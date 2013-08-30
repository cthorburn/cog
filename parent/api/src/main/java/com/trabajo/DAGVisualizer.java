package com.trabajo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DAGVisualizer implements IVisualizer, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<String, DAGGroup> groups=new HashMap<>();

	/**
	 * Serialization constructor
	 */
	public DAGVisualizer() {
		System.out.println("new visualizer!");
	}
	
	
	@Override
  public IVizTaskGroup group(String groupName) {
	  DAGGroup group=groups.get(groupName);
	  if(group==null) {
	  	throw new VizException("no such group: "+groupName);
	  }
	  return group;
  }
	
	@Override
  public IVizTaskContext task(String taskName, String... groupNames) {
		if(!VizValidity.isValidName(taskName)) {
			throw new VizException("invalid task name: "+taskName);
		}
	  return new DAGTaskContext(this, taskName, groupNames);
  }

	@Override
	public String status(IGraphEntityFinder gef) {
		StringBuilder sb=new StringBuilder();
		groups.get("_default").status(gef, sb);
		return sb.toString();
	}
	
	@Override
	public String overview() {
		StringBuilder sb=new StringBuilder();
		groups.get("_default").overview(sb);
		return sb.toString();
	}
	
	@Override
  public void groupTasks(String groupName, String ... taskNames) {
		String[] add=taskNames;
		if("_default".equals(groupName)) {
			add=new String[taskNames.length+1];
			add[0]="Process";
			System.arraycopy(taskNames, 0, add, 1, taskNames.length);
		}
	  groups.put(groupName, new DAGGroup(groupName, this, add));
  }

	DAGGroup getGroup(String groupName) {
	  return groups.get(groupName);
  }

	@Override
  public IVizTaskContext start() {
	  return task("Process", "_default");
  }


	public Collection<DAGGroup> groups() {
	  return groups.values();
  }
	
	public void emitCode(String base, String pkg) {
		for(DAGGroup g: groups.values()) {
			try {
				
	      g.emitCode(base, pkg);
      } catch (IOException e) {
	      e.printStackTrace();
      }
		}
	}
}
