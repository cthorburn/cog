package com.trabajo.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.ScheduleExpression;
import javax.ejb.TimerHandle;
import javax.persistence.EntityManager;

import com.trabajo.TimerType;
import com.trabajo.engine.bobj.NodeTimers;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.ITask;

public class TaskDueDate implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient ITask task;
	private transient String name;
	private int nodeTimerId;
	private ScheduleExpression schedule;

	public TaskDueDate() {
	}

	public TaskDueDate(String name, ScheduleExpression schedule, ITask task) {
		this.name=name;
		this.schedule = schedule;
		this.task = task;
	}

	public INodeTimer getNodeTimer(EntityManager em) {
		return NodeTimers.findById(em, nodeTimerId);
	}

	public ScheduleExpression getSchedule() {
		return schedule;
	}

	public void persist(EntityManager em) {
		INodeTimer nt = NodeTimers.create(em, name, TimerType.DUEDATE, task);
		em.refresh(nt.entity());
		nodeTimerId = nt.getId();
	}

	public void persist(EntityManager em, TimerHandle handle) {
		INodeTimer nt = NodeTimers.findById(em, nodeTimerId);
		nt.setHandle(handle);
		nt.save();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap toMap() {
		HashMap map = new HashMap();
		map.put("serClass", getClass().getName());
		map.put("nodeTimerId", nodeTimerId);
		//map.put("schedule", schedule);
		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	public void fromMap(Map map) {
		String serClass = (String) map.get("serClass");
		if (!serClass.equals(getClass().getName())) {
			throw new IllegalArgumentException(); // map is not for this type; 
		}
		nodeTimerId = (Integer) map.get("nodeTimerId");
		//schedule = (ScheduleExpression) map.get("schedule");
	}
}
