package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;

public class STR {
	int length;
	int padding;
	String str;
	public STR(String str) {
		length = str.getBytes().length;
		padding = 4 - (length%4);
		if(padding == 4) {
			padding = 0;
		}
		this.str = str;
		length += 4 + padding;
	}
	
	public void write(ByteBuffer buffer) {
		buffer.putInt(length);
		buffer.put(str.getBytes());
		for(int i=0; i<padding; i++) {
			buffer.put((byte) 0x00);
		}
	}
}
