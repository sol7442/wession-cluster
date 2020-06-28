package com.wowsanta.wession.impl.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.DATA;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.RaonSessionRequest;
import com.wowsanta.raon.impl.message.AccountDelRequestMessage;
import com.wowsanta.raon.impl.message.AccountListRequestMessage;
import com.wowsanta.raon.impl.message.HelloRequestMessage;
import com.wowsanta.raon.impl.message.RegisterRequestMessage;
import com.wowsanta.raon.impl.message.SessionDelRequestMessage;
import com.wowsanta.raon.impl.message.SessionListRequestMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetRequestMessage;
import com.wowsanta.raon.impl.message.UnkownRequestMessage;
import com.wowsanta.raon.impl.message.UnregisterRequestMessage;
import com.wowsanta.raon.impl.message.UserDataAddRequestMessage;
import com.wowsanta.raon.impl.message.UserDataDelRequestMessage;
import com.wowsanta.raon.impl.message.UserDataGetRequestMessage;
import com.wowsanta.raon.impl.message.UserDataModRequestMessage;
import com.wowsanta.raon.impl.message.VaildateRequestMessage;
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

public class RaonSessionConnection extends NioConnection {

	Stack<RaonSessionMessage> stack = new Stack<>();

	@Override
	public int read() throws ServerException {
		int size = -1;
		SocketAddress address = null;
		try {
			size = client.read(readBuffer);
			address = client.getRemoteAddress();
			
			LOG.application().debug("read : {} / {}",address, readBuffer);
			
			if(size <= 0) {
				return size;
			}
			readBuffer.flip();
			
			do {
				
				RaonSessionMessage request = parse(readBuffer);
				if(request.isComplate()) {
					
					LOG.application().info("request({}):{}/{}",address,request.getCommand(), readBuffer);
					
					ServiceProcess<?, ?> process = createProcess(request);
					if(process != null) {
						process.setConnection(this);
						rquestQueue.put(process);	
					}
				}else {
					stack.push(request);
				}
				
			}while(readBuffer.remaining() > 0 && stack.empty());
			
		} catch (ServerException e) {
			ErrorHanlder(e);	
		} catch (IOException e) {
			throw new ServerException(e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new ServerException(e.getMessage(), e);
		}finally {
			if(readBuffer.remaining() == 0) {
				LOG.application().debug("finally-clear : {}/{}",address, readBuffer);
				readBuffer.clear();
			}else {
				LOG.application().debug("finally-compact : {}/{}",address, readBuffer);
				readBuffer.compact();
			}
		}
		return size;
	}
	
	private void ErrorHanlder(Exception e) {
		try {
			rquestQueue.put(new ErrorProcess(e));
		} catch (InterruptedException e1) {
			LOG.application().error(e.getMessage(),e);
		}	
	}

	private ServiceProcess<?, ?> createProcess(RaonSessionMessage request) {
		ServiceProcess<?, ?> process = null;
		if(request != null) {
			RaonCommand command = request.getCommand();//RaonCommand.valueOf(request.getCommand().getValue());
			switch (command) {
			case CMD_HELLO:
				process = new HelloProcess(request);
				break;
				
			case CMD_PS_ADDUSERDATA:
				process = new UserDataAddProcess(request);
				break;
			case CMD_PS_UPDUSERDATA:
				process = new UserDataModProcess(request);
				break;
			case CMD_PS_DELUSERDATA:
				process = new UserDataDelProcess(request);
				break;
			case CMD_PS_GETUSERDATA:
				process = new UserDataGetProcess(request);
				break;
				
			case CMD_PS_DELACCOUNT:
				process = null;
				break;
			case CMD_PS_DELSESSION:
				process = new UnregisterProcess(request);
				break;
			case CMD_PS_REGISTER:
				process = new RegisterProcess(request);
				break;
			case CMD_PS_SESSIONVALID:
				process = new VaildateProcess(request);
				break;
			case CMD_PS_GETTOKENOTP:
				process = new TokenOtpGetProcess(request);
				break;
				
			case CMD_WRM_ACCOUNTS:
				process = new AccountListProcess(request);
				break;
			case CMD_WRM_DELACCOUNTS:
				process = new AccountDelProcess(request);
				break;
			case CMD_WRM_SESSION:
				process = new SessionListProcess(request);
				break;
			case CMD_WRM_DELSESSION:
				process = new SessionDelProcess(request);
				break;
				
			default:
				process = new UnkownProcess(command.getValue());;
				break;
			}
			
		}else {
			process = new ErrorProcess(new NullPointerException());
		}
		
		return process;
	}

	private RaonSessionMessage parse(ByteBuffer buffer) throws ServerException {
		
		RaonSessionMessage request = null;
		RaonCommand command = null;
		int remind = -1;
		try {
			if(stack.isEmpty()) {
				INT command_value = INT.parse(buffer);
				command = RaonCommand.valueOf(command_value.getValue());
				
				request = command.genRequestMessage();
			}else {
				request = stack.pop();
			}
			
			remind = request.parse(buffer);
		}
		finally {
			LOG.application().debug("{} / {} :",command,remind);
		}
		
		return request;
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
