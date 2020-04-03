package com.wowsanta.wession.impl.server;

import java.nio.ByteBuffer;

public abstract class RaonCommandProcessor {
	public abstract void request(ByteBuffer readBuffer);
	public abstract byte[] response();
	public abstract boolean process();
	
	protected String readStr(ByteBuffer buffer) {
		int str_len = buffer.getInt();
		int padding = str_len%4;
		if(padding == 4) {
			padding = 0;
		}
		
		byte[] str_data = new byte[str_len];
		buffer.get(str_data);
		while(padding > 0) {
			buffer.get();
			padding--;
		}
		return new String(str_data);
	}
	
	protected String readRsrt (ByteBuffer buffer) {
		int str_len = buffer.getInt();
		int padding = str_len%4;
		if(padding == 4) {
			padding = 0;
		}

		byte key = buffer.get();
		
		byte[] str_data = new byte[str_len - 1];
		buffer.get(str_data);
		while(padding > 0) {
			buffer.get();
			padding--;
		}
		return new String(str_data);
	}
}
