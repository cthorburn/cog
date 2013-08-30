package com.trabajo;

import java.util.regex.Pattern;

public class VizValidity {

	private static final Pattern namePat=Pattern.compile("(?i)[a-z][a-z0-9_]{1,128}");
	
	public static boolean isValidName(String name) {
	  return name!=null && namePat.matcher(name).matches();
  }
}
