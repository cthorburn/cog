package com.trabajo.ejb;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.engine.Engine;
import com.trabajo.engine.EngineFactory;
import com.trabajo.engine.TSession;
import com.trabajo.engine.dev.DevHelper;

/**
 * Session Bean implementation class EngineBean
 */
@Stateless
@LocalBean
public class DevBean { 

	private final static Logger logger = LoggerFactory.getLogger(DevBean.class);
	
	@Resource
	private TimerService timerService;
	@Resource(mappedName="java:/trabajo") DataSource dataSource;
	
	@EJB
	private EngineBean engine;

	@PersistenceContext(name = "trabajo")
	private EntityManager em;

	public DevBean() {

	}
	
	@SuppressWarnings("unused")
	private Engine getEngine() {
		return EngineFactory.getEngine();
	}

	public void wipeQuartzTables(TSession ts) {
		try {
			DevHelper.wipeQuartzTables(dataSource.getConnection());
		} catch (SQLException e) {
			ts.getStatus().error("Failed to wipe Quartz tables", logger, e);
		}finally{
			try {
				dataSource.getConnection().close();
			} catch (SQLException e) {
				ts.getStatus().error("Failed to close connection", logger, e);
			}
		}
		
	}
}
