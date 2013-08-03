package com.trabajo.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Strings {
	public static String beforeLast(String s, char c) {
	    return (s.indexOf(c) > 0) ? s.substring(0, s.lastIndexOf(c)):s;
	}

	public static String beforeFirst(String s, char c) {
		return s.substring(0, s.indexOf(c));
	}

	public static String afterLast(String s, char c) {
		return s.substring(s.lastIndexOf(c)+1);
	}

	public static String afterFirst(String s, char c) {
		return s.substring(s.indexOf(c)+1);
	}

	public static Map<String, String> mapFromValuePairDelimitedString(String s, String delim) {
		Map<String, String> map=new HashMap<String, String>();
		
		String[] pairs=splitDelimitedString(s, delim);
		
		for(String p: pairs) {
			String[] ps=p.split("=");
			map.put(ps[0], ps[1]);
		}
		
		return map;
	}

	public static String[] splitDelimitedString(String s, String delim) {
		return s.split(delim);
	}

	public static String knockOff(String s, String toKnockOff) {
		return s.substring(toKnockOff.length());
	}

	public static String knockOffEnd(String s, String toKnockOff) {
		return s.substring(0, s.length()-toKnockOff.length());
	}

	public static String itemAfterNthSlash(int n, String s) {
		return s.startsWith(s) ? s.split("/")[n]:s.split("/")[n+1];
	}

	public static String replaceLast(String s, char c, char d) {
		StringBuilder sb=new StringBuilder();
		sb.append(s);
		sb.setCharAt(s.lastIndexOf(c), d);
		return sb.toString();
	}

    public static boolean isBlank(String s) {
        return s==null || "".equals(s.trim());
    }

    public static String toCSV(Collection<String> strings) {
   	 StringBuilder sb=new StringBuilder();
   	 
   	 for(String s: strings) {
   		 sb.append(s);
   		 sb.append(',');
   		 
   	 }
   	 sb.setLength(sb.length()-1);
   	 return sb.toString();
    }   
    
    public static String toCSV(String[]  strings) {
   	 StringBuilder sb=new StringBuilder();
   	 
   	 for(String s: strings) {
   		 sb.append(s);
   		 sb.append(',');
   		 
   	 }
   	 sb.setLength(sb.length()-1);
   	 return sb.toString();
    }   
 
 

}
