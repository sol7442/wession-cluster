package com.wowsanta.wession.impl.server;

import com.wowsanta.server.Process;

public abstract class RaonCommandProcessor implements Process {

	

	
//	public abstract void request(ByteBuffer readBuffer);
//	public abstract byte[] response();
//	
//	public abstract boolean process();
//	public abstract void parse() throws IOException;
//	public abstract void bind() throws IOException;
//	
//	protected NioConnection connection;
//	public void setConnection(NioConnection connection) {
//		this.connection = connection;		
//	}
//		
//	protected String readStr(ByteBuffer buffer) {
//		int str_len = buffer.getInt();
//		int padding = str_len%4;
//		if(padding == 4) {
//			padding = 0;
//		}
//		
//		byte[] str_data = new byte[str_len];
//		buffer.get(str_data);
//		while(padding > 0) {
//			buffer.get();
//			padding--;
//		}
//		return new String(str_data);
//	}
//	
//	protected String readRsrt (ByteBuffer buffer) {
//		int str_len = buffer.getInt();
//		int padding = str_len%4;
//		if(padding == 4) {
//			padding = 0;
//		}
//
//		@SuppressWarnings("unused")
//		byte key = buffer.get();
//		
//		byte[] str_data = new byte[str_len - 1];
//		buffer.get(str_data);
//		while(padding > 0) {
//			buffer.get();
//			padding--;
//		}
//		return new String(str_data);
//	}
}
