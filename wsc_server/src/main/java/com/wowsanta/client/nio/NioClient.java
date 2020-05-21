package com.wowsanta.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import com.wowsanta.client.Client;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=true)
public abstract class NioClient extends Client {
	protected String ipAddress;
	protected int port;
	
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
