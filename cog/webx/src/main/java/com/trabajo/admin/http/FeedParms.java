package com.trabajo.admin.http;

import java.util.Map;

public interface FeedParms {
	void parse(Map<String, String[]> httpParms);
}
