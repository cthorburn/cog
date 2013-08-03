package com.trabajo.feed.json;

import java.io.Serializable;
import java.util.Map;

import com.trabajo.DefinitionVersion;
import com.trabajo.admin.http.FeedParms;

public class FeedParms_AdPrEdPrEdFm implements FeedParms, Serializable {

	private static final long serialVersionUID = 1L;

	private DefinitionVersion version;
	
	public DefinitionVersion version() {
		return version;
	}

	@Override
	public void parse(Map<String, String[]> httpParms) {
		this.version=DefinitionVersion.parse(httpParms.get("dv")[0]);
	}

}
