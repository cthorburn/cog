package com.trabajo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAGInstance implements IVizInstance, Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, DAGTask> tasks;
	private List<DAGTask> creates;
	private int instanceId;

	public DAGInstance() {

	}

	public DAGInstance(int instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public void creates(String... taskNames) {
		if (creates == null) {
			creates = new ArrayList<DAGTask>();
		}
		creates.clear();
		for (String name : taskNames) {
			creates.add(newTask(name));
		}
	}

	public DAGTask newTask(String name) {
		if (tasks == null) {
			tasks = new HashMap<>();
		}
		DAGTask result = getTask(name);
		if (result == null) {
			result = new DAGTask(this, name);
			tasks.put(name, result);
		}
		return result;
	}

	public DAGTask getTask(String name) {
		return tasks.get(name);
	}

	public void generate1(IGraphEntityFinder gef, StringBuilder sb) {
		sb.append("Instance [id=");
		sb.append(instanceId);
		sb.append(";style=filled;fillcolor=black;fontcolor=white;fontname=Arial;fontsize=10.0;];\n");
		if (tasks != null) {
			for (DAGTask t : tasks.values()) {
				t.generate1(gef, sb);
			}
		}
	}

	public void generate2(IGraphEntityFinder gef, StringBuilder sb) {
		if (creates != null) {
			for (DAGTask t : creates) {
				sb.append("Instance");
				sb.append(" -> ");
				sb.append(t.getName());
				sb.append(";\n");
				t.generate2(gef, sb);
			}
		}
	}

}
