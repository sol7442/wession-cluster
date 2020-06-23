package com.wowsanta.wession.impl.server;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.proc.AbstractSessionProcess;
import com.wowsanta.raon.impl.proc.AccountDelProcess;
import com.wowsanta.raon.impl.proc.AccountListProcess;
import com.wowsanta.raon.impl.proc.HelloProcess;
import com.wowsanta.raon.impl.proc.RegisterProcess;
import com.wowsanta.raon.impl.proc.SessionDelProcess;
import com.wowsanta.raon.impl.proc.SessionListProcess;
import com.wowsanta.raon.impl.proc.TokenOtpGetProcess;
import com.wowsanta.raon.impl.proc.UnregisterProcess;
import com.wowsanta.raon.impl.proc.UserDataAddProcess;
import com.wowsanta.raon.impl.proc.UserDataDelProcess;
import com.wowsanta.raon.impl.proc.UserDataGetProcess;
import com.wowsanta.raon.impl.proc.UserDataModProcess;
import com.wowsanta.raon.impl.proc.VaildateProcess;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceProcess;
import com.wowsanta.server.nio.NioConnection;

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
				
		RaonCommand command = RaonCommand.valueOf(buffer.getInt());
		LOG.application().debug("{} : {}={} ",command, command.getValue(), command.getHexStr());
		
		switch (command) {
		case CMD_HELLO:
			process = new HelloProcess();
			break;
			
		case CMD_PS_ADDUSERDATA:
			process = new UserDataAddProcess();
			break;
		case CMD_PS_UPDUSERDATA:
			process = new UserDataModProcess();
			break;
		case CMD_PS_DELUSERDATA:
			process = new UserDataDelProcess();
			break;
		case CMD_PS_GETUSERDATA:
			process = new UserDataGetProcess();
			break;
			
		case CMD_PS_DELACCOUNT:
			process = null;
			break;
		case CMD_PS_DELSESSION:
			process = new UnregisterProcess();
			break;
		case CMD_PS_REGISTER:
			process = new RegisterProcess();
			break;
		case CMD_PS_SESSIONVALID:
			process = new VaildateProcess();
			break;
		case CMD_PS_GETTOKENOTP:
			process = new TokenOtpGetProcess();
			break;
			
		case CMD_WRM_ACCOUNTS:
			process = new AccountListProcess();
			break;
		case CMD_WRM_DELACCOUNTS:
			process = new AccountDelProcess();
			break;
		case CMD_WRM_SESSION:
			process = new SessionListProcess();
			break;
		case CMD_WRM_DELSESSION:
			process = new SessionDelProcess();
			break;
			
		default:
			process = null;
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
			this.writeBuffer = ByteBuffer.allocate(data.length);
			this.writeBuffer.put(data);
		}catch (Exception e) {
			LOG.application().error(e.getMessage(),e);
			throw new ServerException(e.getMessage(),e);
		}
	}
}
