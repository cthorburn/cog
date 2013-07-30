package com.trabajo.engine;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Msgs {
	
	private final static Msgs instance=new Msgs();
	
	private ResourceBundle msgs;
	
	private ThreadLocal<MessageFormat> formatter; 
	
	private Msgs() { 
		final Locale currentLocale=Locale.getDefault();
		msgs=ResourceBundle.getBundle("Messages", currentLocale);
		
		formatter=new ThreadLocal<MessageFormat>() {

			@Override protected MessageFormat initialValue() {
				return new MessageFormat("", currentLocale);
			}			
		};
	}
	
	public static Msgs instance() {
		return instance;
	}

	public static String getString(String template, Object ... args) {
		MessageFormat f=instance.formatter.get();
		
		String m;
		try {
			m=instance.msgs.getString(template);
		}catch(MissingResourceException e){
			m=new StringBuilder().append("[missing resource: ")
					.append(template)
					.append("]").toString();
		}
		
		f.applyPattern(m);
		
		return f.format(args);
	}

}
