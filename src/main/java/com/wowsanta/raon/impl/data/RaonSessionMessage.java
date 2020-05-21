package com.wowsanta.raon.impl.data;


import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.server.Message;

public abstract class RaonSessionMessage implements Message {
	private static final long serialVersionUID = 8393872998437812701L;
	protected byte[] bytes;
	
	public INT readInt(ByteBuffer buffer) {
		return new INT(buffer.getInt());
	}
	
	public STR readStr(ByteBuffer buffer) {
		return new STR(buffer);
	}
	
	public INDEX readByte4(ByteBuffer buffer) {
		return new INDEX(buffer);
	}
	public RSTRS readRSTS(ByteBuffer buffer) {
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
	
	public abstract CMD getCommand();
	public abstract void parse(ByteBuffer buffer)throws IOException;
}
