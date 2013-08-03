package com.trabajo.engine;

import javax.persistence.EntityManager;

import com.trabajo.IGraphEntityFinder;
import com.trabajo.engine.bobj.Instances;
import com.trabajo.engine.bobj.Tasks;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;

public class GraphEntityFinderImpl implements IGraphEntityFinder {

	private EntityManager em;

	public GraphEntityFinderImpl(EntityManager em) {
		this.em=em;
	}

	@Override
	public ITask findTaskById(int id) {
		return Tasks.findById(em, id);
	}

	@Override
	public IInstance findInstanceById(int id) {
		return Instances.findById(em, id);
	}

}
