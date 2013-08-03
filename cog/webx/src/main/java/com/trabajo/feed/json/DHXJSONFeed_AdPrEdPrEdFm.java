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
import com.trabajo.process.IProcDef;

public class DHXJSONFeed_AdPrEdPrEdFm implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {
		DefinitionVersion dv=((FeedParms_AdPrEdPrEdFm)fp).version();
		
		IProcDef pd=ProcDefs.findByVersion(tsession.getEntityManager(), dv);
		String s=pd.toJson();
		return s;		
	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}
}
