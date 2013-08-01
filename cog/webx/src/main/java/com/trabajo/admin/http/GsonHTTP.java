package com.trabajo.admin.http;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public class GsonHTTP {

	private StringBuilder sb = new StringBuilder();
	private static Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+$");

	public GsonHTTP(HttpServletRequest request, String[] ignore) {

		sb.append("{");

		Set<String> exclude = new HashSet<>();

		for (String s : ignore) {
			exclude.add(s);
		}


		for (String k : request.getParameterMap().keySet()) {
			String v = request.getParameterMap().get(k)[0];

			if (!exclude.contains(k)) {
				sb.append('"');
				sb.append(k);
				sb.append("\": ");

				if (isBoolean(v) || isNumeric(v)) {
					sb.append(v);
				}
				else {
					sb.append('"');
					sb.append(v);
					sb.append("\",");
				}
			}
		}

		sb.setLength(sb.length() - 1);

		sb.append("}");

	}

	public static boolean isNumeric(String number) {
		boolean isValid = false;
		Matcher matcher = pattern.matcher(number);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public boolean isBoolean(String b) {
		return "true".equals(b) || "false".equals(b);
	}

	public <T> T get(Class<T> clazz) {
		return new Gson().fromJson(sb.toString(), clazz);
	}
}
