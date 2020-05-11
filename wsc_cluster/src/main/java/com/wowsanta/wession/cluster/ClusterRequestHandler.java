package com.wowsanta.wession.cluster;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

import com.wowsanta.server.RequestHandler;
import com.wowsanta.server.Request;
import com.wowsanta.server.ServerException;
import com.wowsanta.util.ObjectBuffer;
import com.wowsanta.wession.message.WessionRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClusterRequestHandler implements RequestHandler{
	protected BlockingQueue<Request> requestQueue;
	@Override
	public Request parse(ByteBuffer buffer) throws IOException, ServerException {
		log.debug("buffer : {}", buffer);
		int length = buffer.getInt();
		int remaining = buffer.remaining();
		log.debug("read length : {} / {} ", length , remaining);

		if(length < 1) {
			throw new ServerException("Request Size Error : " + length) ;
		}
				
		if( length > buffer.remaining()) {
			return null;
		}
		
		try {
			byte[] data = new byte[length];
			buffer.get(data);
			log.debug("read buffer : {} -> {}",data.length, data);
			
			return ObjectBuffer.toObject(data,WessionRequest.class);
		} catch (ClassNotFoundException e) {
			throw new ServerException("Request Deserialized Failed : " + e.getMessage() , e) ;
		}
	}

	@Override
	public void bindRquestQueue(BlockingQueue<Request> requestQueue) {
		this.requestQueue = requestQueue;
	}
}
