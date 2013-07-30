package com.trabajo.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.trabajo.DefinitionVersion;
import com.trabajo.IProcessServiceProperties;


public class ProcessServiceProperties implements IProcessServiceProperties, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<DefinitionVersion, Properties> servicePropertiesMap=new HashMap<>();
	private DefinitionVersion processVersion;
	
	public ProcessServiceProperties(DefinitionVersion processVersion) {
		this.processVersion=processVersion;
	}
	
	public void addServiceProperties(DefinitionVersion serviceDv, Properties p) {
		servicePropertiesMap.put(serviceDv, p);
	}

	public DefinitionVersion getProcessVersion() {
		return processVersion;
	}

	public Map<DefinitionVersion, Properties> getMap() {
		return servicePropertiesMap;
	}

	public static IProcessServiceProperties parse(String s) {
		String[] split=s.split("\\|");
		ProcessServiceProperties result=new ProcessServiceProperties(DefinitionVersion.parse(split[0]));

		int i=1;
		
		for(;;) {
			DefinitionVersion sdv=null;
			try{
				String sdvStr=split[i++];
				sdv=DefinitionVersion.parse(sdvStr);
			}catch(ArrayIndexOutOfBoundsException e){
				break;
			}
			Properties p=new Properties(); 
			int cnt=Integer.valueOf(split[i++]);
			
			for(int j=0; j < cnt; j++) {
				p.setProperty(split[i+(j*2)], split[i+(j*2)+1]);
			}
			i+=(cnt*2);
			result.addServiceProperties(sdv, p);
		}
		return result;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();

		sb.append(processVersion);
		sb.append('|');
		
		for(DefinitionVersion sdv: servicePropertiesMap.keySet()) {
			Properties p=servicePropertiesMap.get(sdv);
			int size=p.size();
			
			sb.append(sdv);
			sb.append('|');
			sb.append(size);
			sb.append('|');
			
			for(Object k: p.keySet()) {
				sb.append(k);
				sb.append('|');
				sb.append(p.get(k));
				sb.append('|');
			}
		}
		
		sb.setLength(sb.length()-1);
		return sb.toString();
	}

	@Override
	public void setServiceProperty(DefinitionVersion serviceDv, String key, String value) {
		Properties p=getServiceProperties(serviceDv);
		if(p==null) {
			p=new Properties();
			servicePropertiesMap.put(serviceDv, p);
		}
		p.setProperty(key, value);		
	}

	@Override
	public Properties getServiceProperties(DefinitionVersion serviceDv) {
		return servicePropertiesMap.get(serviceDv);
	}
}
