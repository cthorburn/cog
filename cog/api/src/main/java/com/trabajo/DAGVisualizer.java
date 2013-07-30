package com.trabajo;

import java.io.Serializable;

import javax.persistence.EntityManager;


public class DAGVisualizer implements IVisualizer, Serializable {

	private static final long serialVersionUID = 1L;
	
	private DAGInstance instance;
	
	public static DAGVisualizer DUMMY=new DAGVisualizer();
	
	public DAGVisualizer() {
		
	}
	
	public DAGVisualizer(int instanceId) {
		instance=new DAGInstance(instanceId);
	}
	
	@Override
	public IVizInstance instance() {
		return instance;
	}

	@Override
	public IVizTask task(String taskName) {
		return instance().getTask(taskName);
	}
	
	
	public String generate(IGraphEntityFinder gef) {
		StringBuilder sb=new StringBuilder();
		sb.append("digraph G {\n");
		sb.append("END_PROCESS [id=END_PROCESS;style=filled;fillcolor=black;fontcolor=white;fontname=Arial;fontsize=10.0;];\n");

		((DAGInstance)instance()).generate1(gef, sb);
		((DAGInstance)instance()).generate2(gef, sb);
		sb.append("}\n");
		return sb.toString();
	} 

}
