package com.trabajo.engine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.ScheduleExpression;

import com.trabajo.annotation.Timeout;

public class TimeoutExpression {
	private Timeout timeout;

	public TimeoutExpression(Timeout timeout) {
		super();
		this.timeout = timeout;
	}

	public ScheduleExpression expr(GregorianCalendar dueDate) {
		ScheduleExpression expr=new ScheduleExpression();
		
		if(timeout.repeat()) {
			expr.dayOfMonth(timeout.dayOfMonth());
			expr.dayOfWeek(timeout.dayOfWeek());
			expr.hour(timeout.hour());
			expr.minute(timeout.minute());
			expr.month(timeout.month());
			expr.year(timeout.year());
			expr.start(new Date());
		}
		else {
			int hour=getHour(timeout);
			int minute=getMinute(timeout);
			int month=getMonth(timeout);
			int year=getYear(timeout);
			
			
			GregorianCalendar gc=(GregorianCalendar)dueDate.clone();
			gc.add(Calendar.MINUTE, minute);
			gc.add(Calendar.HOUR, hour);
			gc.add(Calendar.MONTH, month);
			gc.add(Calendar.YEAR, year);
			
			System.out.println("timeout (cal): "+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(gc.getTime())); 
			System.out.println("timeout (due): "+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dueDate.getTime())); 
			
			expr.dayOfMonth(gc.get(Calendar.DAY_OF_MONTH));
			expr.hour(gc.get(Calendar.HOUR_OF_DAY));
			expr.minute(gc.get(Calendar.MINUTE));
			expr.month(gc.get(Calendar.MONTH)+1);
			expr.year(gc.get(Calendar.YEAR));
			
		}

		return expr;
	}

	private int getHour(Timeout t) {
		int result=0;
		if(!"".equals(t.hour())) {
			result=Integer.parseInt(t.hour());
		}	
		return result;
	}
	private int getMinute(Timeout t) {
		int result=0;
		if(!"".equals(t.minute())) {
			result=Integer.parseInt(t.minute());
		}	
		return result;
	}
	private int getMonth(Timeout t) {
		int result=0;
		if(!"".equals(t.month())) {
			result=Integer.parseInt(t.minute());
		}	
		return result;
	}
	private int getYear(Timeout t) {
		int result=0;
		if(!"".equals(t.year())) {
			result=Integer.parseInt(t.year());
		}	
		return result;
	}

}
