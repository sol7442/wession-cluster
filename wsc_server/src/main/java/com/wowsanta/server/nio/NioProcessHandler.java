package com.wowsanta.server.nio;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import com.wowsanta.server.ProcessHandler;
import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class NioProcessHandler implements Runnable , ProcessHandler {
	protected BlockingQueue<Request> requestQueue; 
	
	public void run() {
		try {
			while(true) {
				Request  request = requestQueue.take();
				Response response; 
				
				start();
				try {
					response = read(request);
					proc(request, response);
					write(response);
				} catch (ServerException | IOException e) {
					error(e);
				}finally {
					finish();
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		log.debug("process thread finished : {} ", new Date() );
	}
}
