package com.trabajo.admin.http;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;

public interface Feed {

    String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp);
    void onError(HttpServletRequest request, HttpServletResponse response);
}
