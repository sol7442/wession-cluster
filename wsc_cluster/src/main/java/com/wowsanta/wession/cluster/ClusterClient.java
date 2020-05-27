package com.wowsanta.wession.cluster;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.logger.LOG;
import com.wowsanta.server.Message;
import com.wowsanta.util.ObjectBuffer;


public class ClusterClient extends NioClient {
	public ClusterClient(String ip, int port) {
		super(ip, port);
	}
	
	@Override
	public void write(Message message) throws IOException {
		
		try {
			byte[] array_data = ObjectBuffer.toByteArray(message);
			ByteBuffer buffer = ByteBuffer.allocate(array_data.length + 4);
			buffer.putInt(array_data.length);
			buffer.put(array_data);
			buffer.flip();
			
			int size = socketChannel.write(buffer);

			LOG.application().debug("{} : {} / {} ", this, array_data.length,size);
		}catch (Exception e) {
			LOG.application().error(e.getMessage());
			throw e;
		}finally {
			
		}
	}
	
	@Override
	public <T extends Message> T send(Message reqeust, Class<T> type) throws IOException, ClassNotFoundException {
		write(reqeust);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		socketChannel.read(buffer);
		
		return ObjectBuffer.toObject(buffer.array(),type);
	}

}

