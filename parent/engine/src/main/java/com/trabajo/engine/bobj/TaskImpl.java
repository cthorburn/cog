package com.trabajo.engine.bobj;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import com.trabajo.DAGVisualizer;
import com.trabajo.DeadlineTimescale;
import com.trabajo.IVisualizer;
import com.trabajo.StateContainer;
import com.trabajo.TaskCompletion;
import com.trabajo.TaskStatus;
import com.trabajo.ValidationException;
import com.trabajo.engine.AbstractLifecycle;
import com.trabajo.jpa.NodeJPA;
import com.trabajo.jpa.UserJPA;
import com.trabajo.process.IInstance;
import com.trabajo.process.IRole;
import com.trabajo.process.ITask;
import com.trabajo.process.IUser;
import com.trabajo.values.CreateRoleSpec;

public class TaskImpl extends CogEntity<NodeJPA> implements ITask {

	protected TaskImpl(EntityManager em, NodeJPA node) {
		super(em, node, null);
	}

	protected TaskImpl(EntityManager em, NodeJPA node, AbstractLifecycle l) {
		super(em, node, l);
	}

	@Override
	public void assignToUser(String username) {
		entity().getRoleSet().clear();
		entity().setUserTaskedTo(((UserImpl) Users.findByName(em, username)).entity());
		save();
	}

	@Override
	public void assignToUser(IUser user) {
		entity().getRoles().clear();
		UserJPA ju = (UserJPA) user.entity();
		entity().setUserTaskedTo(ju);
		save();
	}

	@Override
	public IUser getUserAssignedTo() {
		return Users.toImpl(em, entity().getUserTaskedTo());
	}

	@Override
	public void assignToRoles(String... roleNames) {
		entity().setUserTaskedTo(null);

		Set<IRole> roles = new HashSet<>();

		for (String rn : roleNames) {
			IRole r = Roles.findByName(em, rn);
			if (r == null)
				try {
					r = Roles.create(em, new CreateRoleSpec("dynamic", rn, "No description"));
				} catch (ValidationException e) {
					// ignore is OK
				}
			roles.add(r);
		}
		assignToRoles(roles);
	}

	@Override
	public void assignToRoles(Set<IRole> roles) {
		entity().getRoleSet().addAll(Roles.toEntity(roles));
		save();
	}

	@Override
	public Set<IRole> getRolesAssignedTo() {
		return Roles.toImpl(em, entity.getRoles());
	}

	@Override
	public void end(TaskCompletion way, StateContainer parms) {
		entity().setComplete(way);
		entity().getRoles().clear();
		entity().setUserTaskedTo(null);
		em.persist(entity());
		em.flush();
		l.ended(this, parms);
	}

	@Override
	public TaskCompletion getComplete() {
		return entity().getComplete();
	}

	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public IInstance getInstance() {
		return Instances.toImpl(em, entity().getInstance());
	}

	@Override
	public String getNodeClassName() {
		return entity().getNodeClass();
	}

	@Override
	public StateContainer getState() {
		return entity().getNodeState();
	}

	@Override
	public void setStateContainer(StateContainer stateContainer) {
		entity().setNodeState(stateContainer);
	}

	

	@Override
	public void setStatus(TaskStatus status) {
		entity.setStatus(status);
	}

	@Override
	public TaskStatus getStatus() {
		return entity().getStatus();
	}

	@Override
	public void setDeadline(int amount, DeadlineTimescale scale) {
		GregorianCalendar gc = entity().getDateDue();
		
		switch(scale) {
		case DAYS:
			gc.add(Calendar.DAY_OF_YEAR, amount);
			break;
		case HOURS:
			gc.add(Calendar.HOUR_OF_DAY, amount);
			break;
		case MINUTES:
			gc.add(Calendar.MINUTE, amount);
			break;
		case MONTHS:
			gc.add(Calendar.MONTH, amount);
			break;
		case WEEKS:
			gc.add(Calendar.DAY_OF_YEAR, 7*amount);
			break;
		case YEARS:
			gc.add(Calendar.YEAR, amount);
			break;
		default:
			break;
		
		}
		entity().setDateDue(gc);
	}

	@Override
	public GregorianCalendar getDueDate() {
		return entity().getDateDue();
	}

	@Override
	public IUser getOwner() {
		return Users.toImpl(em, entity().getOwner());
	}

	@Override
	public void linkWithVisualizer() {
		/*
		IInstance instance=getInstance();
		IVisualizer v=instance.getVisualizer();
		instance.setVisualizer(DAGVisualizer.DUMMY);
		instance.save();
		
		//TODO This value is used to generate a graph box hyper links
		//in the new task/group scheme the drill down mechanism will be more sophisticated
		//v.task(entity().getName(), entity().getTaskGroup().getName()).setId(entity().getId());
		
		
		
		instance.setVisualizer(v);
		instance.save();
		*/
	}

	@Override
	public void purge() {
		em.remove(entity());
		em.flush();
	}
}
