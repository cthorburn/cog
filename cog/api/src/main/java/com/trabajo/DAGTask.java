package com.trabajo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.trabajo.process.ITask;

public class DAGTask implements IVizTask, Serializable {

	private static final long serialVersionUID = 1L;

	private DAGInstance instance;

	private String name;
	private int taskId;

	private List<DAGTask> creates;
	private List<DAGTask> depends;

	public DAGTask(DAGInstance instance, String name) {
		this.name = name;
		this.instance = instance;
	}

	@Override
	public void creates(String... taskNames) {
		if (creates == null) {
			creates = new ArrayList<DAGTask>();
		}
		creates.clear();
		for (String name : taskNames) {
			creates.add(instance.newTask(name));
		}
	}

	@Override
	public void dependsOn(String... taskNames) {
		if (depends == null) {
			depends = new ArrayList<DAGTask>();
		}
		depends.clear();
		for (String name : taskNames) {
			depends.add(instance.getTask(name));
		}
	}

	@Override
	public void canEndProcess() {
		if (creates == null) {
			creates = new ArrayList<DAGTask>();
		}
		creates.add(instance.newTask("END_PROCESS"));
	}

	public void generate1(IGraphEntityFinder gef, StringBuilder sb) {

		if("END_PROCESS".equals(name)) {
			return;
		}
		
		ITask task = null;

		if (taskId != 0) {
			task = gef.findTaskById(taskId);
		}

		String fill=computeBgcolor(task);
		String fontColor=computeFontcolor(fill);
		sb.append(name);
		sb.append(" [id=");sb.append(taskId);
		sb.append(";URL=\"javascript:detail(");sb.append(taskId);sb.append(")\"");
		sb.append(";style=filled;fillcolor=");sb.append(fill);
		sb.append(";fontcolor=");sb.append(fontColor);
		sb.append(";shape=box;fontname=Arial;fontsize=10.0;];\n");
	}

	private String computeFontcolor(String fill) {
		String result="black";
		
		switch (fill) {
		case "black":
		case "indigo":
			result="white";
		}
		return result;
	}

	private String computeBgcolor(ITask task) {
		String result = "white";
		if (task != null) {
			result = "indigo";
			if (task.getComplete() != null) {
				switch (task.getComplete()) {
				case FAILED:
					result = "crimson";
					break;
				case OK:
					result = "green";
					break;
				}
			}
		}
		return result;
	}

	public void generate2(IGraphEntityFinder gef, StringBuilder sb) {
		if (creates != null) {
			for (DAGTask created : creates) {
				sb.append(name);
				sb.append(" -> ");
				sb.append(created.name);
				sb.append(";\n");
				created.generate2(gef, sb);
			}
		}
		if (depends != null) {
			sb.append("edge [color=red];\n");
			for (DAGTask depend : depends) {
				sb.append(name);
				sb.append(" -> ");
				sb.append(depend.name);
				sb.append(";\n");
			}
			sb.append("edge [color=black];\n");
		}
	}

	public String getName() {
		return name;
	}

	@Override
	public void setId(int id) {
		this.taskId = id;
	}

	public int getTaskId() {
		return taskId;
	}

}
