package com.trabajo.utils;

import java.util.*;

public class DelimitedString {

	private List<String> parts;

	public DelimitedString(String s, String delim) {
		parts = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(s.trim(), delim);

		while (st.hasMoreTokens()) {
			parts.add(st.nextToken().trim());
		}
	}

	public List<String> parts() {
		return parts;
	}
}
