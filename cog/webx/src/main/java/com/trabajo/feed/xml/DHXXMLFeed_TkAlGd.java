package com.trabajo.feed.xml;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.jpa.VersionJPA;

public class DHXXMLFeed_TkAlGd implements Feed {
    
		public String construct(TSession tsession, Engine eng, EntityManager em, FeedParms fp) {
        
        StringBuilder sb=new StringBuilder();
       
        sb.append("<rows>"); 

        //"TaskName,Process,Started By,Importance,Assigned,Due,Note"
    		String jpql = "SELECT NEW com.trabajo.feed.xml.DHXXMLFeed_TkAlGd$GridRow(n.id, n.name, pd.definitionVersion, u.fullName, n.importance, n.dateAssigned, n.dateDue, i.note)"
    				+ " FROM Node n INNER JOIN n.instance i INNER JOIN i.procDef pd INNER JOIN n.userTaskedTo u WHERE u.username=?1  AND n.complete IS NULL AND NOT n.status=com.trabajo.TaskStatus.BARRIERED";
        
    			  //AND NOT n.complete=com.trabajo.TaskCompletion.OK"
        TypedQuery<GridRow> q=em.createQuery(jpql, GridRow.class);

        q.setParameter(1,  tsession.getRemoteUser());
        
        for(GridRow obj: q.getResultList()) {
           obj.processResult(sb, tsession);
        }  
            
        sb.append("</rows>");
        
        return sb.toString();
    }
		
public static class GridRow {
			
			private long id;
			private String name;
			private String version;
			private String initName;
			private String importance;
			private String dateAssigned;
			
			public GridRow(int id, String name, VersionJPA version, String initName, String importance, GregorianCalendar dateAssigned, GregorianCalendar dateDue, String note) {
				super();
				this.id = id;
				this.name = name;
				this.version = version.toString();
				this.initName = initName;
				this.importance = importance;
				SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				this.dateAssigned = df.format(dateAssigned.getTime());
				this.dateDue = df.format(dateDue.getTime());
				this.note = (note==null) ? "":note;
			}

			private String dateDue;
			private String note;
			
			
			public void processResult(StringBuilder sb, TSession ts) {
				
        sb.append("<row id=\""+String.valueOf(id)+"\">");
        //	this.grid.setHeader("Name,Priority,Created,Age (Hours),Due,Overdue");

        appendCell(sb, name); 
        appendCell(sb, version); 
        appendCell(sb, initName);
        appendCell(sb, importance);
        appendCell(sb, dateAssigned);
        appendCell(sb, dateDue);
        appendCell(sb, note);
        
        sb.append("</row>"); 
			}
			
	    private void appendCell(StringBuilder sb, String string) {
        sb.append("<cell><![CDATA[");
        sb.append(string);
        sb.append("]]></cell>");
	    }
		}

    @Override
    public void onError(HttpServletRequest request, HttpServletResponse response) {
        
    }
}

