package com.wowsanta.wession.cluster;

import java.nio.channels.SocketChannel;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ConnectionFactory;
import com.wowsanta.server.ServerException;

public class ClusterConnectionFactory extends ConnectionFactory {
	
	@Override
	public Connection build(SocketChannel channel) throws ServerException {
		ClusterConnection connection = new ClusterConnection();
		connection.initialize();
		connection.setClient(channel);
		connection.rquestQueue = this.requestQueue;
		
		return connection;
	}
}
