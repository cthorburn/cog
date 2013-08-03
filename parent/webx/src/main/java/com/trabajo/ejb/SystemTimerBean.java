package com.trabajo.ejb;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.engine.EngineFactory;

@Stateless
public class SystemTimerBean { 

	private final static Logger logger = LoggerFactory.getLogger(SystemTimerBean.class);
	

	@PersistenceContext(name = "trabajo")
	private EntityManager em;

	public SystemTimerBean() {
	}

	@Schedule(minute="*/2", hour="*", persistent=false)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void sys() {
		
		try {
			if(EngineFactory.getEngine()!=null) {
				EngineFactory.getEngine().systemTimeout(em);
			}
		} catch (Exception e) {
			logger.error("error running timer", e);
		}
	}
}
