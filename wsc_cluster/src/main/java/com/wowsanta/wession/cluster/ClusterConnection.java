package com.wowsanta.wession.cluster;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceProcess;
import com.wowsanta.server.nio.NioConnection;
import com.wowsanta.util.ObjectBuffer;


public class ClusterConnection extends NioConnection {
	
	@Override
	public int read() throws ServerException {
		int size = -1;
		try {
			size = client.read(readBuffer);
			LOG.application().debug("read 0 : {}/{}", size,readBuffer);
			
			if(size <= 0) {
				return size;
			}

			readBuffer.flip();
			LOG.application().debug("flip : {}", readBuffer);
			
			do {
				readBuffer.mark();
				LOG.application().debug("mark : {}", readBuffer);
				
				ClusterMessage message = parse(readBuffer);
				
				if(message == null) {
					readBuffer.reset();
					LOG.application().debug("reset : {}", readBuffer);
					break;
				}else{
					LOG.application().debug("recive : {}", message);
					rquestQueue.put(createProcess(message));
				}
			}while(readBuffer.remaining() > 0);
			
		} catch (Exception e) {
			LOG.application().error("{}",e.getMessage(), e);
			readBuffer.clear();
			throw new ServerException(e.getMessage(),e);
		}finally {
			
			LOG.application().debug("finally : {}/{}", readBuffer  ,readBuffer.remaining());
			
			if(readBuffer.remaining() == 0) {
				readBuffer.clear();
				LOG.application().debug("clear : {}/{}", readBuffer  ,readBuffer.remaining());
			}else {
				readBuffer.compact();
				LOG.application().debug("compact : {}/{}", readBuffer,readBuffer.remaining());
			}
			
		}
		return size;
	}

	private ServiceProcess<?,?> createProcess(ClusterMessage message) {
		AbstractClusterProcess process = null;
		switch (message.getMessageType()) {
		case REGISTER:
			process = new RegisterProcess(message);
			break;
		case CREATE:
			process = new CreateProcess(message);
			break;
		case UPDATE:
			process = new UpdateProcess(message);
			break;
		case DELETE:
			process = new DeleteProcess(message);
			break;
		default:
			process = new ErrorProcess(message);
			break;
		}

		process.setConnection(this);

		return process;
	}

	@Override
	public void write(byte[] data) throws ServerException {
		try {
			this.writeBuffer.put(data);
		}catch (Exception e) {
			LOG.application().error(e.getMessage(),e);
			throw new ServerException(e.getMessage(),e);
		}
	}
	@Override
	public int write() throws ServerException {
		int size  =  -1;
		try {
			this.writeBuffer.flip();
			size = this.client.write(this.writeBuffer);
			this.writeBuffer.clear();
		} catch (IOException e) {
			LOG.application().error(e.getMessage(),e);
			throw new ServerException(e.getMessage(),e);
		}
		return size;
	}
	
	private ClusterMessage parse(ByteBuffer buffer) throws IOException, ServerException {
		int length = buffer.getInt();
		LOG.application().debug("read 1 : {}/{}", length,readBuffer);
		if(length < 1) {
			throw new ServerException("Request Size Error : " + length) ;
		}
				
		if( length > buffer.remaining()) {
			return null;
		}
		
		try {
			byte[] data = new byte[length];
			buffer.get(data);
			LOG.application().debug("read 2 : {}/{}", data.length,readBuffer);
			
			return ObjectBuffer.toObject(data,ClusterMessage.class);
		} catch (ClassNotFoundException e) {
			throw new ServerException("Request Deserialized Failed : " + e.getMessage() , e) ;
		}
	}
}
