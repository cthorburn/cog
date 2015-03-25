package com.trabajo.jpa;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.trabajo.StateContainer;
import com.trabajo.TaskCompletion;
import com.trabajo.TaskStatus;

@Entity(name = "Node")
@Table(name = "NODE")
public class NodeJPA implements JPAEntity<NodeJPA> {

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
		NodeJPA other = (NodeJPA) obj;
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

	public NodeJPA() {
		super();
	}

	private int	id;

	@Override
	@Id
	@SequenceGenerator(name = "NODE_SEQUENCE_GENERATOR", sequenceName = "NODE_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NODE_SEQUENCE_GENERATOR")
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String	nodeClass;

	@Column(name = "NODE_CLASS", nullable = false)
	public String getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}

	private InstanceJPA	instance;
	@ManyToOne(optional=false)
	@JoinColumn(name = "INSTANCE", nullable = false)
	public InstanceJPA getInstance() {
		return instance;
	}

	public void setInstance(InstanceJPA instance) {
		this.instance = instance;
	}

	private UserJPA	owner;

	@ManyToOne(optional=false)
	@Column(name = "OWNER", nullable = false)
	public UserJPA getOwner() {
		return owner;
	}

	public void setOwner(UserJPA owner) {
		this.owner = owner;
	}

	private UserJPA	userTaskedTo;

	@ManyToOne(optional=true)
	@Column(name = "UTASKEDTO", nullable = true)
	public UserJPA getUserTaskedTo() {
		return userTaskedTo;
	}

	public void setUserTaskedTo(UserJPA userTaskedTo) {
		this.userTaskedTo = userTaskedTo;
	}

	@Expose(serialize = false, deserialize = false)
	private StateContainer	nodeState;

	@Lob()
	@Column(name = "NODESTATE", nullable = false)
	public StateContainer getNodeState() {
		return nodeState;
	}

	public void setNodeState(StateContainer nodeState) {
		this.nodeState = nodeState;
	}

	private String	name;

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String	importance;

	@Column(name = "IMPORTANCE", nullable = false)
	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	private String	description;

	@Column(name = "DESCRIPTION", nullable = false, length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private GregorianCalendar	dateAssigned;

	@Column(name = "DATE_ASSIGNED", nullable = false)
	public GregorianCalendar getDateAssigned() {
		return dateAssigned;
	}

	public void setDateAssigned(GregorianCalendar dateAssigned) {
		this.dateAssigned = dateAssigned;
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

	private TaskStatus	status;

	@Column(name = "STATUS", nullable = true, length = 16)
	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	@Expose(serialize = false, deserialize = false)
	public Set<RoleJPA>	roles;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TASK_ROLES", joinColumns = { @JoinColumn(name = "TR_TASK_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "TR_ROLE_ID", referencedColumnName = "ID") })
	public Set<RoleJPA> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleJPA> roles) {
		this.roles = roles;
	}

	public Set<RoleJPA> getRoleSet() {
		Set<RoleJPA> s = getRoles();
		if (s == null) {
			s = new HashSet<RoleJPA>();
			setRoles(s);
		}
		return getRoles();
	}

	private TaskGroupJPA	taskGroup;

	@ManyToOne(optional=false)
	@Column(name = "TASKGROUP", nullable = true)
	public TaskGroupJPA getTaskGroup() {
		return taskGroup;
	}
	public void setTaskGroup(TaskGroupJPA taskGroup) {
		this.taskGroup = taskGroup;
	}
	
	
	private int	groupSequence;
	@Column(name = "GROUPSEQ")
	public int getGroupSequence() {
		return groupSequence;
	}
	public void setGroupSequence(int groupSequence) {
		this.groupSequence = groupSequence;
	}
}
