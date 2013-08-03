package com.trabajo.feed.xml;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.jpa.UserJPA;

public class DHXXMLFeed_AdUsSu implements Feed {

    
    public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {
        
        StringBuilder sb=new StringBuilder();
       
        sb.append("<rows>"); 
        
        Query q=em.createQuery("SELECT u FROM User u", UserJPA.class);

        @SuppressWarnings("unchecked")
        List<UserJPA> results = q.getResultList();
        for(UserJPA u: results) {
            processResult(sb, u);
        }  
            
        sb.append("</rows>");
        
        return sb.toString();
    }

    private void processResult(StringBuilder sb, UserJPA rs) {
        sb.append("<row id=\""+rs.getId()+"\">");
       
        appendCell(sb, rs.getFullName());
        appendCell(sb, rs.getUsername());
        appendCell(sb, rs.getPrimaryEmail());
        sb.append("</row>"); 
    }

    private void appendCell(StringBuilder sb, String string) {
        sb.append("<cell><![CDATA[");
        sb.append(string);
        sb.append("]]></cell>");
    }

    @Override
    public void onError(HttpServletRequest request, HttpServletResponse response) {
        
        
    }
}
