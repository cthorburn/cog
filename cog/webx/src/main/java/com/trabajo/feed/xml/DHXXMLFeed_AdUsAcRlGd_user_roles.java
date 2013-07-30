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
import com.trabajo.utils.XMLSBHelper;

public class DHXXMLFeed_AdUsAcRlGd_user_roles implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

		Query q = em.createNativeQuery("select r.id, r.role_name, r.description, CASE WHEN ur.ur_USER_ID IS NULL THEN 'false' ELSE 'true' END  from role r left outer join user_roles ur on r.id=ur.ur_role_id and ur.ur_user_id=?", Object[].class);
		
		int userid=((FeedParms_AdUsAcRlGd_user_roles)fp).userid();
		
		q.setParameter(1, userid);
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = q.getResultList();
		StringBuilder sb=new StringBuilder();
		XMLSBHelper help=new XMLSBHelper(sb);
		
		help.elm("rows").closeTag();
		for (Object[] objects : results) {
			build(help, objects, userid);
			
		}
		help.closeElm("rows");
		
		return sb.toString();
	}

	private void build(XMLSBHelper help, Object[] objects, int userid) {
		
		help.elm("row").attr("id", String.valueOf(objects[0])).closeTag()
		.elm("cell").closeTag().append(String.valueOf(objects[1])).closeElm("cell")
		.elm("cell").closeTag().append(String.valueOf(objects[2])).closeElm("cell")
		.elm("cell").closeTag().append(String.valueOf(objects[3])).closeElm("cell").closeElm("row");
	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
