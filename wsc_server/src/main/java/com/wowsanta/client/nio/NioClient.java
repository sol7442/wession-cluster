package com.wowsanta.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import com.wowsanta.client.Client;

public abstract class NioClient extends Client {

	String ipAddress;
	int port;
	protected SocketChannel socketChannel;
	
	public NioClient(String ip, int port) {
		this.ipAddress = ip;
		this.port = port;
	}
	
	@Override
	public boolean connect() throws IOException{
		InetSocketAddress address = new InetSocketAddress(this.ipAddress, this.port);
        socketChannel = SocketChannel.open(address);

        return true;
	}

	@Override
	public void close() throws IOException {
		if(socketChannel.isOpen()) {
			socketChannel.close();
		}
	}
}
