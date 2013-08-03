package com.trabajo.feed.json;

import java.io.Serializable;
import java.util.Map;

import com.trabajo.DefinitionVersion;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.utils.Strings;

public class FeedParms_PlEdIn implements FeedParms, Serializable {

	private static final long serialVersionUID = 1L;

	private DefinitionVersion dv;
	
	public DefinitionVersion definitionVersion() {
		return dv;
	}

	@Override
	public void parse(Map<String, String[]> httpParms) {
		String processTreeName=httpParms.get("processTreeName")[0];
		
		String v=Strings.afterLast(processTreeName, '.').replace("-", ".");
		String t1=Strings.beforeLast(processTreeName, '.');
		String t2=Strings.afterLast(t1, '.');
		String t3=t2+"-"+v;
		this.dv=DefinitionVersion.parse(t3);
	}

}
