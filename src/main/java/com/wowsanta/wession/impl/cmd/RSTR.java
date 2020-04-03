package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;

public class RSTR {
	int length;
	int data_len;
	int padding;
	byte key;
	String str;
	public RSTR(byte key, String str) {
		length = str.length() + 1;
		padding = 4 - length%4;
		if(padding == 4) {
			padding = 0;
		}
		this.key = key;
		this.str = str;
		
		data_len = 4 + length + padding;
	}
	
	public void write(ByteBuffer buffer) {
		buffer.putInt(length);
		buffer.put(key);
		buffer.put(str.getBytes());
		for(int i=0; i<padding; i++) {
			buffer.put((byte) 0x00);
		}
	}
}
