package com.trabajo.feed.xml;

import java.io.Serializable;
import java.util.Map;

import com.trabajo.admin.http.FeedParms;

public class FeedParms_AcEdGpEd_group_users implements FeedParms, Serializable {

	private static final long serialVersionUID = 1L;

	private String groupName;
	
	public String groupName() {
		return groupName;
	}

	@Override
	public void parse(Map<String, String[]> httpParms) {
		this.groupName=httpParms.get("groupName")[0];
	}

}
