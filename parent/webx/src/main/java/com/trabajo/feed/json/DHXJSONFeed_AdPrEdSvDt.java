package com.trabajo.feed.json;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.ClassLoaderDescriptor;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;

public class DHXJSONFeed_AdPrEdSvDt implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {
		FeedParms_AdPrEdSvDt parms = (FeedParms_AdPrEdSvDt) fp;
		ClassLoaderDescriptor cld = eng.getClassLoaderDescriptor(parms.version());
		return toJSON(cld);
	}

	private String toJSON(ClassLoaderDescriptor cld) {
		return new Gson().toJson(cld);
	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}
}
