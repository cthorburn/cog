package com.trabajo.feed.xml;

import java.io.Serializable;
import java.util.Map;

import com.trabajo.admin.http.FeedParms;

public class FeedParms_AdUsAcRlGd_user_roles implements FeedParms, Serializable {

	private static final long serialVersionUID = 1L;

	private int userid;
	
	public int userid() {
		return userid;
	}

	@Override
	public void parse(Map<String, String[]> httpParms) {
		this.userid=Integer.parseInt(httpParms.get("userid")[0]);
		
	}

}
