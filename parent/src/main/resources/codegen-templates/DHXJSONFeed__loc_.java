package com.trabajo.feed.json;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.jpa.UserJPA;

public class DHXJSONFeed_${loc} implements Feed {

    public String construct(EntityManager em, FeedParms parms) {
    	FeedParms_${loc} fp=(FeedParms_${loc})parms;
    	
    	//TODO generate JSON response
    	
    	/*
        Query q=em.createQuery("SELECT u FROM User u where u.id=?1", UserJPA.class);

        q.setParameter(1, fp.userid());
		*/
        
        return toJsonString((UserJPA)q.getSingleResult());
    }

    private String toJsonString(UserJPA rs) {
    	return new Gson().toJson(rs);
    }


    @Override
    public void onError(HttpServletRequest request, HttpServletResponse response) {
        
        
    }
}
