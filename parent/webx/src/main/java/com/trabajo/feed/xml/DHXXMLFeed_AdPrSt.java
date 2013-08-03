package com.trabajo.feed.xml;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.jpa.ServiceDefJPA;

public class DHXXMLFeed_AdPrSt implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {
		Query q = em.createQuery("SELECT pd FROM ServiceDef pd", ServiceDefJPA.class);
		@SuppressWarnings("unchecked")
		List<ServiceDefJPA> results = q.getResultList();
		return new ProcessComponentTreeBuilder<ServiceDefJPA>(results, "Services").toString();
	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
