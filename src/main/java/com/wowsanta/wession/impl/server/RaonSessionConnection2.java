package com.wowsanta.wession.impl.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.proc.AbstractSessionProcess;
import com.wowsanta.raon.impl.proc.AccountDelProcess;
import com.wowsanta.raon.impl.proc.AccountListProcess;
import com.wowsanta.raon.impl.proc.ErrorProcess;
import com.wowsanta.raon.impl.proc.HelloProcess;
import com.wowsanta.raon.impl.proc.RegisterProcess;
import com.wowsanta.raon.impl.proc.SessionDelProcess;
import com.wowsanta.raon.impl.proc.SessionListProcess;
import com.wowsanta.raon.impl.proc.TokenOtpGetProcess;
import com.wowsanta.raon.impl.proc.UnkownProcess;
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
import com.wowsanta.util.Hex;

public class RaonSessionConnection2 extends NioConnection {

	@Override
	public int read() throws ServerException {
		int size = -1;
		SocketAddress address = null;
		try {
			size = client.read(readBuffer);
			address = client.getRemoteAddress();
			
			LOG.application().info("read : {}/{}",address, readBuffer);
			
			if(size <= 0) {
				return size;
			}
			readBuffer.flip();
			
			do {
				readBuffer.mark();
				LOG.application().info("mark : {}/{}",address, readBuffer);
				
				
				ServiceProcess<?,?> process = parse(readBuffer);
				
				if(process == null) {
					readBuffer.reset();
					break;
				}else{
					rquestQueue.put(process);
				}
				
			}while(readBuffer.remaining() > 0);
			
		} catch (BufferUnderflowException e) {
			// ignore --- > finally compact.
		} catch (InterruptedException e) {
			throw new ServerException(e.getMessage(),e);
		} catch (IOException e) {
			throw new ServerException(e.getMessage(),e);
		}finally {
			if(readBuffer.remaining() == 0) {
				LOG.application().info("finally-clear : {}/{}",address, readBuffer);
				readBuffer.clear();
			}else {
				LOG.application().info("finally-compact : {}/{}",address, readBuffer);
				readBuffer.compact();
			}
		}
		return size;
	}
	
	private ServiceProcess<?,?> parse(ByteBuffer buffer) throws ServerException ,BufferUnderflowException{
		
		AbstractSessionProcess process = null;
		
		int command_value = buffer.getInt();
		RaonCommand command = RaonCommand.valueOf(command_value);

		LOG.application().info("{} / {} :",command, command_value);
		
		switch (command) {
		case CMD_HELLO:
			process = null;//new HelloProcess();
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
			process = null;//new RegisterProcess();
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
			process = new UnkownProcess(command_value);;
			break;
		}
		 
//		if(process == null) {
//			LOG.application().warn("{} : {} :",command, Hex.toHexString(buffer.array()));
//			throw new ServerException("UNKOWN REQUEST : " + command);
//		}
		
		process.setConnection(this);
		try {
			RaonSessionMessage message = (RaonSessionMessage) process.getRequest().getMessage();
			message.parse(buffer);
		} catch (Exception e) {
			//throw new ServerException("PARSER ERROR : " + e.getMessage(), e);
			process = new ErrorProcess(e);;
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
