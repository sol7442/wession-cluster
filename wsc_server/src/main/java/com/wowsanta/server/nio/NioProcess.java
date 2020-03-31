package com.wowsanta.server.nio;

import java.util.concurrent.Callable;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class NioProcess implements Callable<NioConnection> {
	public NioConnection connection;
	@Override
	public NioConnection call() throws Exception {
		try {
			read();
			run();
			write();
		}catch (Exception e) {
			log.error("process : ",e);
		}
		return connection;
	}
	public abstract void read();
	public abstract void run();
	public abstract void write();
	
}
