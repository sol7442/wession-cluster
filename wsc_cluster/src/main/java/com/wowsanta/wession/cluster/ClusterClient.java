package com.wowsanta.wession.cluster;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.server.Message;
import com.wowsanta.server.Response;
import com.wowsanta.util.ObjectBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=true)
public class ClusterClient extends NioClient {
	public ClusterClient(String ip, int port) {
		super(ip, port);
	}

	@Override
	public void write(Message message) throws IOException {
		byte[] array_data = ObjectBuffer.toByteArray(message);
		
		ByteBuffer buffer = ByteBuffer.allocate(array_data.length + 4);
		buffer.putInt(array_data.length);
		buffer.put(array_data);
		buffer.flip();
		
		log.debug("request data length : {} / {} ", array_data.length,buffer);
		socketChannel.write(buffer);
	}
	
	@Override
	public Message send(Message message) throws IOException, ClassNotFoundException {
		write(message);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		socketChannel.read(buffer);
		
		return ObjectBuffer.toObject(buffer.array(),Message.class);
	}

}

