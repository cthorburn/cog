package com.trabajo.feed.xml;

import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trabajo.DefinitionVersion;
import com.trabajo.IProcessServiceProperties;
import com.trabajo.admin.http.Feed;
import com.trabajo.admin.http.FeedParms;
import com.trabajo.engine.Engine;
import com.trabajo.engine.TSession;
import com.trabajo.utils.XMLSBHelper;

public class DHXXMLFeed_AdPrEdPrEd implements Feed {

	public String construct(TSession ts, Engine eng, EntityManager em, FeedParms fp) {
		FeedParms_AdPrEdPrEd parms=(FeedParms_AdPrEdPrEd)fp;
		IProcessServiceProperties psp=eng.getProcessServiceProperties(ts, parms.version());
		return toDHXXML(psp);
	}

	private String toDHXXML(IProcessServiceProperties psp) {
		StringBuilder sb=new StringBuilder();
		XMLSBHelper xml=new XMLSBHelper(sb);
		xml.elm("rows").closeTag();

		Map<DefinitionVersion, Properties> map=psp.getMap();
		
		for(DefinitionVersion dv: map.keySet()) {
			Properties p=map.get(dv);
			if(p.size()==0) {
				xml.elm("row").attr("id", dv.toString()).closeTag()
				.elm("cell").closeTag().append(dv.toString()).closeElm("cell")
				.emptyElm("cell")
				.emptyElm("cell")
				.closeElm("row");
			}
			else {
				int i=0;
						
				for(Object o: p.keySet()) {
					String key=(String)o;
					String value=p.getProperty(key);
					
					if(i==0) {
						xml.elm("row").attr("id", dv.toString()+"|"+key).closeTag()
						.elm("cell").closeTag().append(dv.toString()).closeElm("cell")
						.elm("cell").closeTag().append(key).closeElm("cell")
						.elm("cell").closeTag().CDATA(value).closeElm("cell")
						.closeElm("row");
					}
					else {
						xml.elm("row").attr("id", dv.toString()+"|"+key).closeTag()
						.emptyElm("cell")
						.elm("cell").closeTag().append(key).closeElm("cell")
						.elm("cell").closeTag().CDATA(value).closeElm("cell")
						.closeElm("row");
					}
					i++;
				}		
						
			}
		}
		xml.closeElm("rows");
		 
		return sb.toString();
	}

	@Override
	public void onError(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
}
