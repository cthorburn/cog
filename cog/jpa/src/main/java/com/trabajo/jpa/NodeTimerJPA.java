package com.trabajo.jpa;

import java.util.GregorianCalendar;

import javax.ejb.TimerHandle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.trabajo.TimerType;

/**
 * Generated Class DO NOT MODIFY
 */
@Entity(name = "NodeTimer")
@Table(name = "NODETIMER")
public class NodeTimerJPA implements JPAEntity<NodeTimerJPA>{

	private static final long serialVersionUID = 1L;

	public NodeTimerJPA() {
		super();
	}
	
	private int id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	private TimerType timerType;
	
	@Column(name = "TIMERTYPE")
	public TimerType getTimerType() {
		return timerType;
	}
	public void setTimerType(TimerType timerType) {
		this.timerType = timerType;
	}

	private TimerHandle handle;

	@Column(name = "HANDLE", nullable = true)
	public TimerHandle getHandle() {
		return handle;
	}
	public void setHandle(TimerHandle handle) {
		this.handle=handle;
	}
	
	private GregorianCalendar cancelled;
	@Column(name = "CANCELLED", nullable = true)
	public GregorianCalendar getCancelled() {
		return cancelled;
	}
	public void setCancelled(GregorianCalendar cancelled) {
		this.cancelled = cancelled;
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
	
	private String callback;
	
	@Column(name = "CALLBACK", nullable=true)
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	private String name;

	@Column(name = "NAME", nullable=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
