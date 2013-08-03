package com.trabajo.annotation;

import com.trabajo.ValidationException;
import com.trabajo.utils.Strings;

public class DHXSelectOptionsParser {

	private StringBuilder sb=new StringBuilder();
	
	public DHXSelectOptionsParser(String value) throws ValidationException {
		// TODO permit commas by escaping eg: \,
		
		String[] sp=value.split(",");
		
		if(sp.length % 2 !=0 ) {
			throw new ValidationException("invalid option spec");
		}
	
		for(int i=0; i<sp.length; i+=2) {
			sb.append("<option ");

			if(sp[i].startsWith("*") || sp[i+1].startsWith("*")) {
				sp[i]=Strings.afterLast(sp[i], '*');
				sp[i+1]=Strings.afterLast(sp[i+1], '*');
				sb.append("selected=\"true\" ");
			}
			sb.append("text=\"");
			sb.append(sp[i+1]);
			sb.append("\" value=\"");
			sb.append(sp[i]); 
			sb.append("\"/>");
		}
	}

	@Override
	public String toString() {
		return sb.toString();
	}

}
