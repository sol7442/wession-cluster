package com.wowsanta.server;


import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import com.wowsanta.util.config.JsonConfiguration;

public abstract class ConnectionFactory extends JsonConfiguration {
	protected BlockingQueue<Request> requestQueue;
	
	public abstract Connection build(SocketChannel channel) throws ServerException;
	public void bindRquestQueue(BlockingQueue<Request> queue) {
		this.requestQueue = queue;
	}
}
