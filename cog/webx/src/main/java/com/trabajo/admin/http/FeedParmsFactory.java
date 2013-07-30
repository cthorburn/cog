package com.trabajo.admin.http;

public class FeedParmsFactory {

	public static FeedParms getJson(String feed) {
		
		try {
			return (FeedParms)Class.forName("com.trabajo.feed.json.FeedParms_"+feed).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static FeedParms getXml(String feed) {
		
		try {
			return (FeedParms)Class.forName("com.trabajo.feed.xml.FeedParms_"+feed).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

}
