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
import com.trabajo.jpa.GroupJPA;

public class DHXXMLFeed_AcGpTr implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

		TypedQuery<GroupJPA> q = em.createQuery("SELECT g FROM Group g", GroupJPA.class);

		List<GroupJPA> results = q.getResultList();

		return new CategorisedTreeBuilder<GroupJPA>(results, "Groups").toString();

	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
