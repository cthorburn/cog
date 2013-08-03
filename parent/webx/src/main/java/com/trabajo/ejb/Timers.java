package com.trabajo.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

@Startup
@Singleton
public class Timers {

	@Resource
	private TimerService timerService;

	
	@PostConstruct
	public void ps() {
		init();
	}
	
	public void init() {
		
		System.out.println("--- Timer info ------------------------------------------------------------");
		for(Timer timer: timerService.getTimers()) {
			
			System.out.println("is persistent: "+timer.isPersistent());
		}
		System.out.println("--- Timer info ------------------------------------------------------------");
	}
	
	@Timeout
	public void timeout() {}
	
}
