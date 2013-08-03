package com.trabajo.utils;

public class XMLSBHelper {
	private StringBuilder sb;
	
	public XMLSBHelper(StringBuilder sb) {
		this.sb=sb;
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	public XMLSBHelper elm(String s) {
		sb.append('<');
		sb.append(s);
		
		return this;
	}
	
	public XMLSBHelper attr(String a, String b) {
		sb.append(' ');
		sb.append(a);
		sb.append("=\"");
		sb.append(b);
		sb.append('"');
		
		return this;
	}

	public XMLSBHelper  child(String s) {
		sb.append("><");
		sb.append(s);
		
		return this;
	}

	public XMLSBHelper closeTag() {
		sb.append(">");
		return this;
	}
	public XMLSBHelper closeEmptyTag() {
		sb.append("/>");
		return this;
	}
	public XMLSBHelper closeElm(String s) {
		sb.append("</");
		sb.append(s);
		sb.append('>');
		return this;
	}

	public XMLSBHelper append(String s) {
		sb.append(s);
		return this;
	}

	public XMLSBHelper emptyElm(String s) {
		elm(s);
		closeEmptyTag();
		return this;
	}

	public XMLSBHelper CDATA(String value) {
		append("<![CDATA[");
		append(value);
		append("]]>");
		return this;
	}
	
}
