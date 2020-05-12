package com.wowsanta.wession.cluster;

import java.nio.channels.SocketChannel;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ConnectionFactory;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClusterConnectionFactory extends ConnectionFactory {
	private int bufferSize = 1024;
	
	@Override
	public Connection build(SocketChannel channel) throws ServerException {
		ClusterConnection connection = new ClusterConnection();
		connection.initialize(bufferSize);
		connection.setClient(channel);
		connection.rquestQueue = this.requestQueue;
		
		log.info("Connected to : {} ", connection);

		return connection;
	}
}
