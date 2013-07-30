package com.trabajo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.trabajo.TimerType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD } )
public @interface Timeout {

	String name();
	//ScheduleExpression data
	String dayOfMonth() default "";
	String dayOfWeek() default "";
	String hour() default "";
	String minute() default "";
	String month() default "";
	String year() default "";

	// if DUEDATE then repeat must be false day* data must be "" and the 
	// rest are considered increments from the due date value
	//
	// if ADHOC then all data is passed to ScheduleExpression as is 
	TimerType type();
	
	
	// if repeat, the time will not be cancelled until the process does it explicitly
	// if not. the timer is cancelled before the timeout method is invoked
	boolean repeat();
}
