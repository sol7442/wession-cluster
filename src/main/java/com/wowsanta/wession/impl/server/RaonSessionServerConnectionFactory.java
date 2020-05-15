package com.wowsanta.wession.impl.server;

import java.nio.channels.SocketChannel;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ConnectionFactory;
import com.wowsanta.server.ServerException;

public class RaonSessionServerConnectionFactory extends ConnectionFactory{

	@Override
	public Connection build(SocketChannel channel) throws ServerException {
		RaonSessionConnection connection = new RaonSessionConnection();
		connection.initialize(this.bufferSize);
		connection.setClient(channel);
		connection.setRquestQueue(this.requestQueue);
		
		return connection;
	}
}
