package com.wowsanta.server;


import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import com.wowsanta.util.config.JsonConfiguration;

public abstract class ConnectionFactory extends JsonConfiguration {
	protected BlockingQueue<ServiceProcess<?,?>> requestQueue;
	protected int bufferSize;
	public abstract Connection build(SocketChannel channel) throws ServerException;
	public void bindRquestQueue(BlockingQueue<ServiceProcess<?,?>> queue) {
		this.requestQueue = queue;
	}
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
}
