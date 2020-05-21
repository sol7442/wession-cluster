package com.wowsanta.server;

import java.util.concurrent.BlockingQueue;

import com.wowsanta.logger.LOG;
import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class ServiceDispatcher extends JsonConfiguration implements Runnable {
	
	protected BlockingQueue<ServiceProcess<?,?>> requestQueue;
	private boolean runable = true;

	public void bindRquestQueue(BlockingQueue<ServiceProcess<?,?>> queue) {
		this.requestQueue = queue;
	}
	
	public void stop() {
		runable = false;
		requestQueue.notifyAll();
	}
	
	public void run() {
		while(runable) {
			try {
				ServiceProcess<?,?> pocess = requestQueue.take();
				
				before(pocess);
				dispatcher(pocess);
				after(pocess);
				
			} catch (InterruptedException e) {
				LOG.application().error(e.getMessage(),e);
			}
		}
	}
	public abstract void before(ServiceProcess<?,?> request);
	public abstract void dispatcher(ServiceProcess<?,?> request);
	public abstract void after(ServiceProcess<?,?> request);
}
