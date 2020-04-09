package com.wowsanta.wession.impl.data;

import java.nio.ByteBuffer;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RSTR extends DATA {
	private byte idx;
	private String data;
	private int padding;

	public RSTR(byte idx, String str) {
		int data_len = str.getBytes().length + 1;
		padding = 4 - (data_len%4);
		if(padding == 4) {
			padding = 0;
		}
		this.idx  = idx;
		this.data = str;
		
		length = 4 + data_len + padding;
	}
	
	public static RSTR read(ByteBuffer buffer) {
		int data_len = buffer.getInt();
		byte idx = buffer.get();
		int padding = 4 - data_len%4;
		if(padding == 4) {
			padding = 0;
		}
		
		byte[] str_data = new byte[data_len - 1];
		buffer.get(str_data);
		while(padding > 0) {
			buffer.get();
			padding--;
		}
		return new RSTR(idx, new String(str_data));
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		int data_len = this.data.length() + 1;
		int padding = 4 - data_len%4;
		if(padding == 4) {
			padding = 0;
		}
		
		buffer.putInt(data_len);
		buffer.put(this.idx);
		buffer.put(data.getBytes());
		for(int i=0; i<padding; i++) {
			buffer.put((byte) 0x00);
		}
	}
}
