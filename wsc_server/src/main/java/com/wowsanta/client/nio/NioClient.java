package com.wowsanta.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.wowsanta.client.Client;
import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.util.Buffer;

public class NioClient extends Client {

	String ipAddress;
	int port;
	SocketChannel socketChannel;
	
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

	@Override
	public void write(Request reqeust) throws IOException {
		socketChannel.write(Buffer.toBuffer(reqeust));
		
	}
	public void write(ByteBuffer buffer) throws IOException {
		socketChannel.write(buffer);
	}
	@Override
	public Response send(Request reqeust) throws IOException, ClassNotFoundException {
		socketChannel.write(Buffer.toBuffer(reqeust));
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		socketChannel.read(buffer);
		
		return Buffer.toObject(buffer.array(),Response.class);
	}


}
