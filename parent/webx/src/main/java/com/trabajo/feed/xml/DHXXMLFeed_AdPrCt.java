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
import com.trabajo.jpa.ClassLoaderDefJPA;

public class DHXXMLFeed_AdPrCt implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

		Query q = em.createQuery("SELECT pd FROM ClassLoaderDef pd", ClassLoaderDefJPA.class);

		@SuppressWarnings("unchecked")
		List<ClassLoaderDefJPA> results = q.getResultList();

		return new ProcessComponentTreeBuilder<ClassLoaderDefJPA>(results, "ClassLoaders").toString();

	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
