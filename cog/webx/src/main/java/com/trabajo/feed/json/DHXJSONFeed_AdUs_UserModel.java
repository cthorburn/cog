package com.trabajo.feed.json;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.jpa.UserJPA;

public class DHXJSONFeed_AdUs_UserModel implements Feed {

    public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms parms) {
    	FeedParms_AdUs_UserModel fp=(FeedParms_AdUs_UserModel)parms;
    	
        Query q=em.createQuery("SELECT u FROM User u where u.id=?1", UserJPA.class);

        q.setParameter(1, fp.userid());

        
        return toJsonString((UserJPA)q.getSingleResult());
    }

    private String toJsonString(UserJPA rs) {
    	return new Gson().toJson(rs);
    }


    @Override
    public void onError(HttpServletRequest request, HttpServletResponse response) {
        
        
    }
}
