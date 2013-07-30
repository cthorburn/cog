package com.anyone;

import java.util.GregorianCalendar;

public interface Note {
	GregorianCalendar getCreateTime();
	String getCreator();
	String getNoteText();
}
