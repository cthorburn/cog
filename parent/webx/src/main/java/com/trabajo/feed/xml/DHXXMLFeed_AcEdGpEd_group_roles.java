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

public class DHXXMLFeed_AcEdGpEd_group_roles implements Feed {

	public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

		String groupName=((FeedParms_AcEdGpEd_group_roles)fp).groupName();
		
		Query q = em.createNativeQuery("select r.id, r.role_name, r.description, CASE WHEN gr.gr_role_id IS NULL  THEN 'false' ELSE 'true' END  from 	TGroup g inner join Group_roles gr on g.id=gr.gr_group_id and g.GROUP_NAME=?1 right outer join ROLE r on r.id=gr.gr_role_id", Object[].class);
		
		q.setParameter(1, groupName);

		@SuppressWarnings("unchecked")
		List<Object[]> results = q.getResultList();
		StringBuilder sb=new StringBuilder();
		XMLSBHelper help=new XMLSBHelper(sb);

		help.elm("rows").closeTag();
		for (Object[] objects : results) {
			build(help, objects);
			
		}
		help.closeElm("rows");
		
		return sb.toString();
	}

	private void build(XMLSBHelper help, Object[] objects) {
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
