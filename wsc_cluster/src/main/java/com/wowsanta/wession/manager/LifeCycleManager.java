package com.wowsanta.wession.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class LifeCycleManager implements Runnable {
	private transient ScheduledExecutorService scheduler; 
	private transient boolean initialized = false;
	
	int delay  = 10;
	int period = 10;
	
	public boolean initialize() {
		if(!initialized) {
			scheduler = Executors.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(this, delay, period, TimeUnit.MINUTES);
			scheduler.execute(this);
		}
		
		return initialized;
	}
}
