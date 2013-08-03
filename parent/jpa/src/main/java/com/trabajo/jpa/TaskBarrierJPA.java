package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.trabajo.StateContainer;
import com.trabajo.process.TaskChronology;

@Entity(name = "TaskBarrier")
@Table(name = "TASKBARRIER")
public class TaskBarrierJPA implements JPAEntity<TaskBarrierJPA> {



	private static final long serialVersionUID = 1L;

	public TaskBarrierJPA() {
		super();
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {

	}

	private int id;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String dependency;

	@Column(name = "DEPENDENCY", nullable = false)
	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	private NodeJPA task;
	@ManyToOne
	@Column(name = "TASK", nullable = false)
	public NodeJPA getTask() {
		return task;
	}

	public void setTask(NodeJPA task) {
		this.task = task;
	}
	
	private NodeJPA dependencyTask;
	@ManyToOne
	@Column(name = "DEPENDENCYTASK", nullable = true)
	public NodeJPA getDependencyTask() {
		return dependencyTask;
	}
	public void setDependencyTask(NodeJPA dependencyTask) {
		this.dependencyTask = dependencyTask;
	}
	
	private TaskChronology chronology;
	@Column(name = "CHRONOLOGY", nullable = false, length=10)
	public TaskChronology getChronology() {
		return chronology;
	}
	public void setChronology(TaskChronology chronology) {
		this.chronology = chronology;
	}

	private StateContainer parms;
	@Lob
	@Column(name = "PDATA", nullable = true)
	public StateContainer getParms() {
		return parms;
	}
	public void setParms(StateContainer parms) {
		this.parms = parms;
	}
	
	private boolean satisfied;
	@Column(name = "SATISFIED", nullable = false )
	public boolean getSatisfied() {
		return satisfied;
	}

	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}
}
