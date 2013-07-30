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
import com.trabajo.jpa.ClassLoaderDefJPA;
import com.trabajo.jpa.RoleJPA;

public class DHXXMLFeed_AdUsAcRlTr implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

		TypedQuery<RoleJPA> q = em.createQuery("SELECT r FROM Role r", RoleJPA.class);

		List<RoleJPA> results = q.getResultList();

		return new CategorisedTreeBuilder<RoleJPA>(results, "Roles").toString();

	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
