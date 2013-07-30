package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.jpa.TaskUploadJPA;
import com.trabajo.process.ITaskUpload;

public class TaskUploadImpl  extends CogEntity<TaskUploadJPA> implements ITaskUpload  {
	
	public TaskUploadImpl(EntityManager em, TaskUploadJPA entity) {
		super(em, entity, null);
	}


	@Override
	public boolean getVersioned() {
		return entity().getVersioned();
	}


	@Override
	public String getPath() {
		return entity().getPath();
	}


	@Override
	public String getDocSet() {
		return entity().getDocSet();
	}


	@Override
	public String getOriginalName() {
		return entity().getOriginalName();
	}


	@Override
	public int getVersion() {
		return entity().getVersion();
	}

}
