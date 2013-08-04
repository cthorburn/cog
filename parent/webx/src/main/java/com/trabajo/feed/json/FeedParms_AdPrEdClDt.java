package com.trabajo.feed.json;

import java.io.Serializable;
import java.util.Map;

import com.trabajo.DefinitionVersion;
import com.trabajo.admin.http.FeedParms;

public class FeedParms_AdPrEdClDt implements FeedParms, Serializable {

	private static final long serialVersionUID = 1L;

	private DefinitionVersion dv;
	
	public DefinitionVersion version() {
		return dv;
	}

	@Override
	public void parse(Map<String, String[]> httpParms) {
		
		//convert from form "a.b.c.name.versions.1-2-3" to form "name-1.2.3"
		String treeFormat=httpParms.get("dv")[0];
		
		String [] tf=treeFormat.split("\\.");
		
		String dvFormat=tf[tf.length-2]+"-"+tf[tf.length-1].replaceAll("-", ".");
		
		this.dv=DefinitionVersion.parse(dvFormat);
		
	}

}
