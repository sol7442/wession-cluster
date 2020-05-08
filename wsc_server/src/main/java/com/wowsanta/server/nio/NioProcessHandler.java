package com.wowsanta.server.nio;

import java.util.Date;
import java.util.concurrent.Callable;


import com.wowsanta.server.ProcessHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class NioProcessHandler implements Callable<NioProcessHandler> , ProcessHandler {
	protected NioConnection connection;
	public NioProcessHandler bind(NioConnection conneciton) {
		this.connection = conneciton;
		return this;
	}
	
	@Override
	public NioProcessHandler call() throws Exception {
		try {
			log.debug("run 1 : {} ", this.connection);
			run();
			log.debug("run 2 : {} ", this.connection);
		}catch (Exception e) {
			error(e);
		}finally {
		}
		return this;
	}
}
