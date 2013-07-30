package com.trabajo.feed.xml;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;

public class DHXXMLFeed_menu implements Feed {

    
    public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {

    	if(tsession.userHasRole("admin")) {
    	
    	return 
        		"<menu>"+
        		"	<item id=\"admin\" text=\"Admin\">"+ 
        		"		<item id=\"admin:users\" text=\"Users\" />"+ 
        		"		<item id=\"admin:process\" text=\"Processes\" />"+ 
        		"		<item id=\"admin:instance\" text=\"Instances\" />"+ 
        		"		<item id=\"admin:acl\" text=\"Accces Control\" /> "+
        		"	</item> "+
        		"	<item id=\"processes\" text=\"Processes\">"+ 
        		"		<item id=\"processes:proclist\" text=\"Process List\"/>"+ 
        		"		<item id=\"processes:tasklist\" text=\"Task List\"/>"+ 
        		"		<item id=\"processes:history\" text=\"History\"/> "+
        		"</item> "+
        		"	<item id=\"reports\" text=\"Reports\">"+ 
        		"		<item id=\"reports:run\" text=\"Run\"/>"+ 
        		"</item> "+
        		"	<item id=\"account\" text=\"Account\">"+ 
        		"		<item id=\"account:settings\" text=\"Settings\"/>"+ 
        		"	</item> "+
        		"	<item id=\"system\" text=\"System\">"+ 
        		"		<item id=\"system:configure\" text=\"Configure\"/>"+ 
        		"		<item id=\"system:dev\" text=\"Development Tools\"/>"+ 
        		"	</item> "+
        		"</menu>";        		
    	}
    	else {
      	return 
        		"<menu>"+
        		"	<item id=\"processes\" text=\"Processes\">"+ 
        		"		<item id=\"processes:proclist\" text=\"Process List\"/>"+ 
        		"		<item id=\"processes:tasklist\" text=\"Task List\"/>"+ 
        		"		<item id=\"processes:messages\" text=\"Messages\"/> "+
        		"		<item id=\"processes:history\" text=\"History\"/> "+
        		"		<item id=\"processes:browse\" text=\"Browse\"/> "+
        		"</item> "+
        		"	<item id=\"account\" text=\"Account\">"+ 
        		"		<item id=\"account:settings\" text=\"Settings\"/>"+ 
        		"	</item> "+
        		"</menu>";        		
    	}
    }

    @Override
    public void onError(HttpServletRequest request, HttpServletResponse response) {
        
        
    }
}
