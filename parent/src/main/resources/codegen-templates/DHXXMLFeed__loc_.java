package com.trabajo.feed.xml;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;

public class DHXXMLFeed_${loc} implements Feed {

    
    public String construct(EntityManager em, FeedParms parms) {
        
    	FeedParms_${loc} fp=(FeedParms_${loc})parms;

        String s=
        		"<?xml version='1.0' encoding='UTF-8'?>" + 
        		"<tree id=\"0\">" + 
        		"	<item text=\"Process 1\" id=\"1\" child=\"1\">" + 
        		"		<userdata name=\"desc\">desc Process 1</userdata>" + 
        		"		<item text=\"Process 1.1\" id=\"2\" child=\"1\">" + 
        		"			<item text=\"Process 1.1.1\" id=\"3\" child=\"0\"/>" + 
        		"		</item>" + 
        		"	</item>" + 
        		"	<item text=\"Process 2\" id=\"4\" child=\"1\">" + 
        		"		<userdata name=\"desc\">desc Process 2</userdata>" + 
        		"		<item text=\"Process 2.1\" id=\"5\" child=\"0\">" + 
        		"			<item text=\"Process 2.1.1\" id=\"6\" child=\"1\"/>" + 
        		"		</item>" + 
        		"	</item>" + 
        		"</tree>";
       
        return s;
    }

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
