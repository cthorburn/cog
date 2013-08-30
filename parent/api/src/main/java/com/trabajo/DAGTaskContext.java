package com.trabajo;

public class DAGTaskContext implements IVizTaskContext {
	private String taskName;
	private String[] groupNames;
	private DAGVisualizer viz;
	
	public DAGTaskContext( DAGVisualizer viz, String taskName, String[] groupNames) {
	  super();
	  this.taskName = taskName;
	  this.groupNames = (groupNames==null || groupNames.length==0) ? new String[] {"_default"}:groupNames;
	  this.viz = viz;
  }

	@Override
  public void createsTasks(final String... taskNames) {
		iterateGroupNames(
				new DAGTaskVisitor() {
					public void visit(DAGTask t) {
			  		t.createsTasks(taskNames);
					}
				}
		); 
  }

	@Override
  public void createsGroups(final String... groupNames) {
			iterateGroupNames(
					new DAGTaskVisitor() {
						public void visit(DAGTask t) {
				  		t.createsGroups(groupNames);
						}
					}
			); 
  }

	@Override
  public void dependsOn(final String ... taskNames) {
		iterateGroupNames(
				new DAGTaskVisitor() {
					public void visit(DAGTask t) {
			  		t.dependsOn(taskNames);
					}
				}
		); 
  }

	
	@Override
  public void canEndProcess() {
		iterateGroupNames(
				new DAGTaskVisitor() {
					public void visit(DAGTask t) {
						t.canEndProcess();
					}
				}
		); 
  }
	
	private void iterateGroupNames(DAGTaskVisitor v) {
  	for(String name: groupNames) {
  		DAGGroup g=viz.getGroup(name);
  		if(g==null) {
  			throw new IllegalStateException("group not previously declared: "+name);
  		}
  		DAGTask t=g.getTask(taskName);
  		if(t==null) {
  			throw new IllegalStateException("task: "+taskName+ " not previously associated with group: "+name);
  		}
  		v.visit(t);
  	}	
	}

	@Override
  public void dependsGroups(final String... groups) {
		iterateGroupNames(
				new DAGTaskVisitor() {
					public void visit(DAGTask t) {
			  		t.dependsGroup(groups);
					}
				}
		); 
  }
}

interface DAGTaskVisitor {
	void visit(DAGTask t);
}