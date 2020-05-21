package com.wowsanta.wession.impl.server;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.HellowRequestMessage;
import com.wowsanta.raon.impl.message.RegisterRequestMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetRequestMessage;
import com.wowsanta.raon.impl.message.UnregisterRequestMessage;
import com.wowsanta.raon.impl.message.UserDataAddRequestMessage;
import com.wowsanta.raon.impl.message.UserDataDelRequestMessage;
import com.wowsanta.raon.impl.message.UserDataGetRequestMessage;
import com.wowsanta.raon.impl.message.VaildateRequestMessage;
import com.wowsanta.raon.impl.proc.AbstractSessionProcess;
import com.wowsanta.raon.impl.proc.HelloProcess;
import com.wowsanta.raon.impl.proc.RegisterProcess;
import com.wowsanta.raon.impl.proc.TokenOtpGetProcess;
import com.wowsanta.raon.impl.proc.UnregisterProcess;
import com.wowsanta.raon.impl.proc.UserDataAddProcess;
import com.wowsanta.raon.impl.proc.UserDataDelProcess;
import com.wowsanta.raon.impl.proc.UserDataGetProcess;
import com.wowsanta.raon.impl.proc.VaildateProcess;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceProcess;
import com.wowsanta.server.nio.NioConnection;
import com.wowsanta.util.Hex;

public class RaonSessionConnection extends NioConnection {

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
				
				ServiceProcess<?,?> process = parse(readBuffer);
				if(process == null) {
					readBuffer.reset();
					LOG.application().debug("reset : {}", readBuffer);
					break;
				}else{
					LOG.application().debug("recive : {}", process.getRequest().getMessage());
					rquestQueue.put(process);
				}
			}while(readBuffer.remaining() > 0);
			
		} catch (Exception e) {
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
	
	private ServiceProcess<?,?> parse(ByteBuffer buffer) throws ServerException {
		
		AbstractSessionProcess process = null;
				
		int command = buffer.getInt();
		LOG.application().debug("command : {} ", Hex.toHexString(command));
		
		switch (command) {
		case 1:
			process = new HelloProcess(new HellowRequestMessage());
			break;
		case 66304:
			process = new UnregisterProcess(new UnregisterRequestMessage());
			break;
		case 67072:
			process = new RegisterProcess(new RegisterRequestMessage());
			break;
		case 68096:
			process = new VaildateProcess(new VaildateRequestMessage());
			break;
		case 65792:
			process = new UserDataAddProcess(new UserDataAddRequestMessage());
			break;
		case 65795:
			process = new UserDataGetProcess(new UserDataGetRequestMessage());
			break;
		case 65794:
			process = new UserDataDelProcess(new UserDataDelRequestMessage());
			break;
		case 68097:
			process = new TokenOtpGetProcess(new TokenOtpGetRequestMessage());
			break;
		default:
			break;
		}
		 
		if(process == null) {
			throw new ServerException("UNKOWN REQUEST : " + command);
		}
		
		process.setConnection(this);
		
		try {
			RaonSessionMessage message = (RaonSessionMessage) process.getRequest().getMessage();
			message.parse(buffer);
		} catch (IOException e) {
			throw new ServerException("PARSER ERROR : " + e.getMessage(), e);
		}
		
		return process;
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

	@Override
	public void write(byte[] data) throws ServerException {
		try {
			this.writeBuffer.put(data);
		}catch (Exception e) {
			LOG.application().error(e.getMessage(),e);
			throw new ServerException(e.getMessage(),e);
		}
	}
}
