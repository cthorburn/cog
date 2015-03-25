package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.trabajo.DAGVisualizer;
import com.trabajo.ProcessCompletion;
import com.trabajo.StateContainer;

/**
 * Generated Class DO NOT MODIFY
 */
@Entity(name = "Instance")
@Table(name = "INSTANCE")
public class InstanceJPA implements JPAEntity<InstanceJPA> {


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getInstantiation();
		result = prime * result + ((getProcDef() == null) ? 0 : getProcDef().hashCode());
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
		InstanceJPA other = (InstanceJPA) obj;
		if (getInstantiation() != other.getInstantiation())
			return false;
		if (getProcDef() == null) {
			if (other.getProcDef() != null)
				return false;
		} else if (!getProcDef().equals(other.getProcDef()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public InstanceJPA() {
		super();
	}

	private int id;

	@Id
	@SequenceGenerator(name = "INSTANCE_SEQUENCE_GENERATOR", sequenceName = "INSTANCE_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTANCE_SEQUENCE_GENERATOR")
	@Column(name = "ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}

	private ProcessCompletion complete;	
	@Column(name = "COMPLETE", nullable=true, length=16)
	public ProcessCompletion getComplete() {
		return complete;
	}
	public void setComplete(ProcessCompletion way) {
		complete=way;
		
	}

	private int instantiation;
	@Column(name = "INSTANTIATION", nullable = false)
	public int getInstantiation() {
		return instantiation;
	}
	public void setInstantiation(int instantiation) {
		this.instantiation = instantiation;
	}

	private UserJPA initiator;

	@ManyToOne(optional=false)
	@JoinColumn(name = "INITIATOR", nullable = false)
	public UserJPA getInitiator() {
		return initiator;
	}

	public void setInitiator(UserJPA initiator) {
		this.initiator = initiator;
	}

	private ProcDefJPA procDef;

	@ManyToOne(optional=false)
	@Column(name = "PROCDEF", nullable = false)
	public ProcDefJPA getProcDef() {
		return procDef;
	}

	public void setProcDef(ProcDefJPA procDef) {
		this.procDef = procDef;
	}

	private String note;

	@Column(name = "NOTE", nullable = true, length = 512)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Expose(serialize = false, deserialize = false)
	private StateContainer instanceState;

	@Lob
	@Column(name = "INSTANCESTATE", nullable = false)
	public StateContainer getInstanceState() {
		return instanceState;
	}

	public void setInstanceState(StateContainer instanceState) {
		this.instanceState = instanceState;
	}
}
