package com.wowsanta.raon.impl.data;


import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.Message;
import com.wowsanta.server.ServerException;

public abstract class RaonSessionMessage implements Message {
	private static final long serialVersionUID = 8393872998437812701L;
	protected byte[] bytes;
	
	public INT readInt(ByteBuffer buffer) throws BufferUnderflowException {
		return new INT(buffer.getInt());
	}
	
	public STR readStr(ByteBuffer buffer) throws ServerException, BufferUnderflowException{
		return new STR(buffer);
	}
	
	public BYTE4 readByte4(ByteBuffer buffer) throws BufferUnderflowException{
		return new BYTE4(buffer);
	}
	public RSTRS readRSTS(ByteBuffer buffer) throws BufferUnderflowException{
		return new RSTRS(buffer);
	}
	
	
		
	public int readCommand() {
		return new INT(bytes,0).getValue();
	}
	public INT readInt(int idx) {
		return new INT(bytes, idx);
	}
	public STR readStr(int idx) {
		return new STR(bytes,idx);
	}
	
	public abstract int parse(ByteBuffer buffer)throws ServerException,BufferUnderflowException;
	public abstract boolean isComplate();

	public abstract RaonCommand getCommand();
}
