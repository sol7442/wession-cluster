package com.wowsanta.wession.cluster;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

import com.wowsanta.server.Request;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.nio.NioConnection;
import com.wowsanta.util.ObjectBuffer;
import com.wowsanta.wession.message.WessionRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClusterConnection extends NioConnection {
	protected BlockingQueue<Request> rquestQueue ;
	
	@Override
	public int read() throws ServerException {
		int size = -1;
		try {
			size = client.read(readBuffer);
			log.debug("read 0 : {}/{}", size,readBuffer);
			
			if(size <= 0) {
				return size;
			}

			readBuffer.flip();
			log.debug("flip : {}", readBuffer);
			
			do {
				readBuffer.mark();
				log.debug("mark : {}", readBuffer);
				
				Request request = parse(readBuffer);
				if(request == null) {
					readBuffer.reset();
					log.debug("reset : {}", readBuffer);
					break;
					
				}else{
					request.setConnection(this);
					rquestQueue.put(request);
				}
			}while(readBuffer.remaining() > 0);
			
		} catch (Exception e) {
			log.error("{}",e.getMessage(), e);
			readBuffer.clear();
			throw new ServerException(e.getMessage(),e);
		}finally {
			
			log.debug("finally : {}/{}", readBuffer  ,readBuffer.remaining());
			
			if(readBuffer.remaining() == 0) {
				readBuffer.clear();
				log.debug("clear : {}/{}", readBuffer  ,readBuffer.remaining());
			}else {
				readBuffer.compact();
				log.debug("compact : {}/{}", readBuffer,readBuffer.remaining());
			}
			
		}
		return size;
	}

	@Override
	public int write() throws ServerException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private Request parse(ByteBuffer buffer) throws IOException, ServerException {
		int length = buffer.getInt();
		log.debug("read 1 : {}/{}", length,readBuffer);
		if(length < 1) {
			throw new ServerException("Request Size Error : " + length) ;
		}
				
		if( length > buffer.remaining()) {
			return null;
		}
		
		try {
			byte[] data = new byte[length];
			buffer.get(data);
			log.debug("read 2 : {}/{}", data.length,readBuffer);
			
			return ObjectBuffer.toObject(data,WessionRequest.class);
		} catch (ClassNotFoundException e) {
			throw new ServerException("Request Deserialized Failed : " + e.getMessage() , e) ;
		}
	}
	
}
