package com.wowsanta.raon.impl.data;


import java.nio.ByteBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper=true)
@Slf4j
public class STR extends DATA{
	String value;
	
	public STR(ByteBuffer buffer) {
		try {
			this.length  = buffer.getInt();
			this.padding = getPadding(length);
			this.size    = INT.LENGTH + this.length + this.padding;
			byte[] data = new byte[this.length];
			buffer.get(data);
			
			for(int i=0; i<padding; i++) {
				buffer.get();
			}
			this.value = new String(data);	
		}catch (Exception e) {
			log.debug("{}/",buffer,this);
			log.error(e.getMessage(), e);
		}
	}
	public STR(String str) {
		byte[] byte_data = str.getBytes();
		
		this.length  = byte_data.length;
		this.padding = getPadding(length);
		this.size    = INT.LENGTH + this.length + this.padding;
		
		this.value = str;
	}
	
	public STR(byte[] bytes, int idx) {
		this.length  = new INT(bytes,idx).getValue();
		this.padding = getPadding(length);
		this.size    = INT.LENGTH + this.length + this.padding;
		
		this.value   = new String(bytes, idx + INT.LENGTH, this.length); 
	}
	@Override
	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(this.size);
		
		buffer.putInt(this.length);
		buffer.put(this.value.getBytes());
		
		for(int i=0; i<padding; i++) {
			buffer.put((byte) 0x00);
		}
		return buffer.array();
	}

	
	
	
//	@Override
//	public byte[] toBytes() {
//		ByteBuffer buffer = ByteBuffer.allocate(this.length);
//		buffer.putInt(this.length);
//		buffer.put(data.getBytes());
//		
//		for(int i=0; i<padding; i++) {
//			buffer.put((byte) 0x00);
//		}
//		
//		return buffer.array();
//	}
//	
//	public static STR read(NioConnection connection) throws IOException {
//		INT length  = INT.read(connection);
//		int padding = getPadding(length.value);
//		
//		byte[] str_data = new byte[length.value];
//		connection.read(str_data);
//		
//		while(padding > 0) {
//			connection.read(new byte[1]);
//			padding--;
//		}
//		
//		return new STR(new String(str_data));
//	}
//	public static STR read(ByteBuffer buffer) {
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
//		return new STR(new String(str_data));
//	}
//
//	public void write(ByteBuffer buffer) {
///****************************************************************************************
// * EUC-KR은  Policy Server에서 지원하지 않는다.
// * 
////		try {
////			System.out.println("length 1 : " + length);
////			System.out.println("length 2 : " + data.getBytes("UTF-8").length);
////			System.out.println("length 3 : " + data.getBytes("EUC-KR").length);
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
//****************************************************************************************/		
//		buffer.putInt(length);
//		buffer.put(data.getBytes());
//		for(int i=0; i<padding; i++) {
//			buffer.put((byte) 0x00);
//		}
//	}

	
}
