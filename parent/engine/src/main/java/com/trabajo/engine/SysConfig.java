package com.trabajo.engine;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.google.gson.Gson;
import com.trabajo.jpa.ConfigJPA;

public class SysConfig implements Serializable {

	public boolean isObliterateOnRestart() {
		return obliterateOnRestart;
	}

	private static final long serialVersionUID = 1L;

	private String GraphVizDir;
	private String FileCacheDir;
	private String rdbms;
	private boolean obliterateOnRestart;
	private boolean development;

	public SysConfig(EntityManager em) {
		TypedQuery<ConfigJPA> q = em.createQuery("Select c from SysConfig c", ConfigJPA.class);

		List<ConfigJPA> l = q.getResultList();

		for (ConfigJPA jpa : l) {
			String key = jpa.getKey();
			String value = jpa.getValue();

			switch (key) {
			case "GraphVizDir":
				this.GraphVizDir = value;
				break;
			case "FileCacheDir":
				this.FileCacheDir = value;
				break;
			case "rdbms":
				this.rdbms = value;
				break;
			case "obliterateOnRestart":
				this.obliterateOnRestart = Boolean.parseBoolean(value);
				break;
			case "development":
				this.development = Boolean.parseBoolean(value);
				break;
			}
		}
	}

	public String toJSon() {
		return new Gson().toJson(this);
	}

	public void save(EntityManager em) {
		TypedQuery<ConfigJPA> q = em.createQuery("Select c from SysConfig c where c.key=?1", ConfigJPA.class);
		update(em, q, "GraphVizDir", GraphVizDir);
		update(em, q, "FileCacheDir", FileCacheDir);
		update(em, q, "ObliterateOnRestart", String.valueOf(obliterateOnRestart));
		update(em, q, "development", String.valueOf(development));
		update(em, q, "rdbms", rdbms);
		em.flush();
	}

	private void update(EntityManager em, TypedQuery<ConfigJPA> q, String key, String value) {
		ConfigJPA jpa;

		try {
			q.setParameter(1, key);
			jpa = q.getSingleResult();
			jpa.setValue(value);
			em.persist(jpa);
		} catch (NoResultException e) {
			jpa = new ConfigJPA();
			jpa.setKey(key);
			jpa.setValue(value);
			em.persist(jpa);
		}
	}

	public String getGraphVizDir() {
		return GraphVizDir;
	}

	public void setGraphVizDir(String parameter) {
		this.GraphVizDir = parameter;
	}

	public void setFileCacheDir(String parameter) {
		this.FileCacheDir = parameter;

	}

	public void setRdbms(String parameter) {
		this.rdbms = parameter;
	}

	public String getFileCacheDir() {
		return FileCacheDir;
	}

	public String getRdbms() {
		return rdbms;
	}

	public void setObliterateOnRestart(boolean obliterateOnRestart) {
		this.obliterateOnRestart=obliterateOnRestart;
	}

	public void setDevelopment(boolean development) {
		this.development = development;
	}
	public boolean isDevelopment() {
		return development;
	}

	public String getJackRabbitURL() {
		// TODO Auto-generated method stub
		return "http://localhost:8200/rmi";
	}

	public File getFSLocation() {
		// TODO Auto-generated method stub
		return new File(new File(System.getProperty("user.home")), ".trabajo");
	}
}
