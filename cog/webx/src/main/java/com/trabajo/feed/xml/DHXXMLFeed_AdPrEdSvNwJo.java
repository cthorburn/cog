package com.trabajo.feed.xml;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.ClassLoaderUploadManager;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;

public class DHXXMLFeed_AdPrEdSvNwJo implements Feed {

	public String construct(TSession ts, Engine eng, EntityManager em, FeedParms fp) {
		ClassLoaderUploadManager clum=ts.getClassLoaderUploadManager();
		return clum.getJarOrder(((FeedParms_AdPrEdSvNwJo)fp).version(), "Sv");
	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
