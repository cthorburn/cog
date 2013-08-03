package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.trabajo.ILifecycle;
import com.trabajo.engine.AbstractLifecycle;
import com.trabajo.jpa.JPAEntity;

public abstract class CogEntity<J extends JPAEntity<J>> {

	protected EntityManager em;
	protected J entity;
	protected AbstractLifecycle l;
	
	public CogEntity(EntityManager em, J entity, AbstractLifecycle l) {
		this(em, entity);
		this.l=l;
	}

	public CogEntity(EntityManager em, J entity) {
		super();
		this.em = em;
		this.entity = entity;
	}

	public J entity() {
		return entity;
	}
	
	public void save() {
		em.persist(entity());
		em.flush();
	}
	public int getId() {
		return entity().getId();
	}
	public void setLifecycle(ILifecycle  l) {
		this.l=(AbstractLifecycle)l;
	}

	public String toJson() {
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(entity());
		jsonElement.getAsJsonObject().addProperty("ok", true);
		String s= gson.toJson(jsonElement);
		return s;
	}
}
