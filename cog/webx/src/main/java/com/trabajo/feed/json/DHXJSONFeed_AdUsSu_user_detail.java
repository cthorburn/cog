package com.trabajo.feed.json;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.engine.bobj.Users;

public class DHXJSONFeed_AdUsSu_user_detail implements Feed {

    
    public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {
        int userid=((FeedParms_AdUsSu_user_detail)fp).userid();
        return Users.findById(tsession.getEntityManager(), userid).toJson();
    }

    @Override
    public void onError(HttpServletRequest request, HttpServletResponse response) {
        
        
    }
}
