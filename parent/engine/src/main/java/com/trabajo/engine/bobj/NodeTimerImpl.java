package com.trabajo.engine.bobj;

import java.util.GregorianCalendar;

import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.TimerHandle;
import javax.persistence.EntityManager;

import com.trabajo.TimerType;
import com.trabajo.jpa.NodeTimerJPA;
import com.trabajo.process.INodeTimer;
import com.trabajo.process.ITask;

public class NodeTimerImpl extends CogEntity<NodeTimerJPA> implements INodeTimer {
	
	public NodeTimerImpl(EntityManager em, NodeTimerJPA entity) {
		super(em, entity, null);
	}

	@Override
	public ITask getTask() {
		return Tasks.toImpl(em, entity().getTask());
	}

	@Override
	public TimerType getTimerType() {
		return entity().getTimerType();
	}

	@Override
	public TimerHandle getHandle() {
		return entity().getHandle();
	}

	@Override
	public void cancel() {
		
		TimerHandle th=getHandle();
		try{
			th.getTimer().cancel();
		}	
		catch(NoSuchObjectLocalException x) {
			//ignore, expected
		}

		entity().setCancelled(new GregorianCalendar());
	}

	@Override
	public void setHandle(TimerHandle handle) {
		entity().setHandle(handle);
	}

	@Override
	public void delete() {
		em.remove(entity());
	}
}
