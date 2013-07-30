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

public class DHXXMLFeed_AdUsAcGpGd_user_groups implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

		int userid=((FeedParms_AdUsAcGpGd_user_groups)fp).userid();
		
	//	TypedQuery<Object[]> q = em.createQuery("SELECT DISTINCT g.id, g.name, g.description, u.id from User u LEFT OUTER JOIN u.groups g WHERE u.id=?1 or g.id IS NOT NULL", Object[].class);
		Query q = em.createNativeQuery("select g.id, g.group_name, g.description, CASE WHEN gu.gu_user_id IS NULL THEN 'false' ELSE 'true' END  from tGROUP g left outer join Group_users gu on g.id=gu.gu_group_id and gu.gu_user_id=?", Object[].class);
		
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
