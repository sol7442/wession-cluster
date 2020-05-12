package com.wowsanta.server;

import java.util.concurrent.BlockingQueue;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class ServiceDispatcher extends JsonConfiguration implements Runnable {
	
	protected BlockingQueue<Request> requestQueue;
	private boolean runable = true;
	
	public void stop() {
		runable = false;
		requestQueue.notifyAll();
	}
	public void run() {
		log.debug("ServiceDispatcher : RUN - {}", Thread.currentThread().getName());
		while(runable) {
			try {
				dispatcher(requestQueue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		log.debug("finish service : {} ", Thread.currentThread().getName());
	}
	
	public abstract void dispatcher(Request request);
	public void bindRquestQueue(BlockingQueue<Request> queue) {
		this.requestQueue = queue;
	}
}
