package com.trabajo.jpa;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.trabajo.TaskCompletion;

@Entity(name = "TaskGroup")
@Table(name = "TASKGROUP")
public class TaskGroupJPA implements JPAEntity<TaskGroupJPA> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getInstance() == null) ? 0 : getInstance().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskGroupJPA other = (TaskGroupJPA) obj;
		if (getInstance() == null) {
			if (other.getInstance() != null)
				return false;
		} else if (!getInstance().equals(other.getInstance()))
			return false;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	private static final long	serialVersionUID	= 1L;

	public TaskGroupJPA() {
		super();
	}

	private int	id;

    @Override
	@Id
	@SequenceGenerator(name = "TASKGROUP_SEQUENCE_GENERATOR", sequenceName = "TASKGROUP_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASKGROUP_SEQUENCE_GENERATOR")
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private InstanceJPA	instance;

	@ManyToOne
	@Column(name = "INSTANCE", nullable = false)
	public InstanceJPA getInstance() {
		return instance;
	}

	public void setInstance(InstanceJPA instance) {
		this.instance = instance;
	}

	private String	name;

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private int	taskGroupSequence;

	@Column(name = "TASKGROUPSEQ")
	public int getTaskGroupSequence() {
		return taskGroupSequence;
	}

	public void setTaskGroupSequence(int taskGroupSequence) {
		this.taskGroupSequence = taskGroupSequence;
	}
	
	private String	description;

	@Column(name = "DESCRIPTION", nullable = true, length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private GregorianCalendar	dateCreated;

	@Column(name = "DATE_CREATED", nullable = false)
	public GregorianCalendar getDateAssigned() {
		return dateCreated;
	}

	public void setDateAssigned(GregorianCalendar dateCreated) {
		this.dateCreated = dateCreated;
	}

	private GregorianCalendar	dateDue;

	@Column(name = "DATE_DUE", nullable = true)
	public GregorianCalendar getDateDue() {
		return dateDue;
	}

	public void setDateDue(GregorianCalendar dateDue) {
		this.dateDue = dateDue;
	}

	private TaskCompletion	complete;

	@Column(name = "COMPLETE", nullable = true, length = 16)
	public TaskCompletion getComplete() {
		return complete;
	}

	public void setComplete(TaskCompletion way) {
		complete = way;

	}

	private Set<NodeJPA> tasks;
	@OneToMany(mappedBy="taskGroup", fetch=FetchType.EAGER)
	public Set<NodeJPA> getTasks() {
	  return tasks;
  }
	public void setTasks(Set<NodeJPA> tasks) {
	  this.tasks=tasks;
  }
}
