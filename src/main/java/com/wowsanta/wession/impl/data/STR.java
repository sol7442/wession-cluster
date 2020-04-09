package com.wowsanta.wession.impl.data;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class STR extends DATA{
	private String data;
	private int padding;
	
	public STR(String str) {
		length = str.getBytes().length;
		padding = 4 - (length%4);
		if(padding == 4) {
			padding = 0;
		}
		this.data = str;
		length += 4 + padding;
	}
	
	public static STR read(ByteBuffer buffer) {
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
		return new STR(new String(str_data));
	}

	@Override
	public void write(ByteBuffer buffer) {
/****************************************************************************************
 * EUC-KR은  Policy Server에서 지원하지 않는다.
 * 
//		try {
//			System.out.println("length 1 : " + length);
//			System.out.println("length 2 : " + data.getBytes("UTF-8").length);
//			System.out.println("length 3 : " + data.getBytes("EUC-KR").length);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
****************************************************************************************/		
		buffer.putInt(length);
		buffer.put(data.getBytes());
		for(int i=0; i<padding; i++) {
			buffer.put((byte) 0x00);
		}
	}
	
}
