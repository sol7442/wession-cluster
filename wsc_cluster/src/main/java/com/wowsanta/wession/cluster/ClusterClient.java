package com.wowsanta.wession.cluster;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.util.ObjectBuffer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClusterClient extends NioClient {
	public ClusterClient(String ip, int port) {
		super(ip, port);
	}

	@Override
	public void write(Request reqeust) throws IOException {
		byte[] array_data = ObjectBuffer.toByteArray(reqeust);
		
		ByteBuffer buffer = ByteBuffer.allocate(array_data.length + 4);
		buffer.putInt(array_data.length);
		buffer.put(array_data);
		buffer.flip();
		
		log.debug("request data length : {} / {} ", array_data.length,buffer);

		
		socketChannel.write(buffer);
		
//		// header - [data-size]/[data-body]
//		socketChannel.write(ByteBuffer.wrap(new byte[4]).putInt(array_data.length + 4));
//		socketChannel.write(ByteBuffer.wrap(array_data));
		
	}
	
	@Override
	public Response send(Request reqeust) throws IOException, ClassNotFoundException {
		write(reqeust);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		socketChannel.read(buffer);
		
		return ObjectBuffer.toObject(buffer.array(),Response.class);
	}

}

