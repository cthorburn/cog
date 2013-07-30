package com.trabajo.feed.json;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.DefinitionVersion;
import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.engine.bobj.ProcDefs;

public class DHXJSONFeed_PlEdIn implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {
		DefinitionVersion dv=((FeedParms_PlEdIn)fp).definitionVersion();
		
    return ProcDefs.findByVersion(tsession.getEntityManager(), dv).toJson();
	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}
}
