package com.trabajo.admin.http;

import com.trabajo.ejb.EngineBean;
import com.trabajo.engine.TSession;

public class DHTMLX {

	private EngineBean engine;
	
	public DHTMLX(EngineBean engine) {
		super();
		this.engine = engine;
	}

	public String xml(TSession tsession, String feed, FeedParms fp) {
		return engine.xml(tsession, feed, fp);
	}
	
	public String json(TSession tsession, String feed, FeedParms fp) {
		String s= engine.json(tsession, feed, fp);
		return s;
	}

}
