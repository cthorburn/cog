package com.trabajo.engine;

import java.util.List;

import javax.persistence.EntityManager;

import com.trabajo.process.SearchSpec;

public class Search<T>  {
	private SearchSpec searchSpec; 
	private EntityManager em;
	
	public Search(SearchSpec searchSpec, EntityManager em) {
		// TODO Auto-generated constructor stub
	}

	public T findSingle() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> findMultiple() {
		// TODO Auto-generated method stub
		return null;
	}
}
