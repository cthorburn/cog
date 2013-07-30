package com.trabajo.utils;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

	public static String toLocalDateTimeString(GregorianCalendar dateAssigned, TimeZone zone) {
		try {
			SimpleDateFormat df=new SimpleDateFormat();
			df.setTimeZone(zone);
			return df.format(dateAssigned);
		} catch (Exception e) {
			return "";
		}
	}

}
