package com.trabajo.feed.xml;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.jpa.ReportDefJPA;

public class DHXXMLFeed_RpTr implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

		TypedQuery<ReportDefJPA> q = em.createQuery("SELECT r FROM ReportDef r", ReportDefJPA.class);

		List<ReportDefJPA> results = q.getResultList();

		return new ProcessComponentTreeBuilder<ReportDefJPA>(results, "Reports").toString();

	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
