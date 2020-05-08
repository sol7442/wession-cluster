package com.wowsanta.wession.impl.data;

import java.io.IOException;

import com.wowsanta.server.nio.NioConnection;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class INT extends DATA {
	public static final int  LENGTH = 4;
	
	int value;
	public INT(int value) {
		this.value  = value;
		this.length = LENGTH;
	}
	
	public INT(byte[] bytes, int idx) {
		this.value = 	(
				(((int)bytes[idx + 0] & 0xff) << 24) |
				(((int)bytes[idx + 1] & 0xff) << 16) |
				(((int)bytes[idx + 2] & 0xff) << 8)  |
				(((int)bytes[idx + 3] & 0xff)));
		this.length = 4;
	}
	
//	public INT(byte[] bytes) {
//		this.value = 	(
//				(((int)bytes[0] & 0xff) << 24) |
//				(((int)bytes[1] & 0xff) << 16) |
//				(((int)bytes[2] & 0xff) << 8)  |
//				(((int)bytes[3] & 0xff)));
//		this.length = 4;
//	}
//	@Override
//	public byte[] toBytes(){
//		byte[] bytes= new byte[this.length];
//		
//		bytes[0] = (byte)(value >> 24);
//		bytes[1] = (byte)(value >> 16);
//		bytes[2] = (byte)(value >> 8);
//		bytes[3] = (byte)(value);
//		
//		return bytes;
//	}
//	public static INT read(NioConnection connection) throws IOException {
//		byte[] data = new byte[4];
//		connection.read(data);
//		return new INT(data);
//	}
	
//	
//
//	
//	public static INT read(ByteBuffer buffer) {
//		int value = buffer.getInt();
//		return new INT(value);
//	}
//	
//	@Override
//	public void write(ByteBuffer buffer) {
//		buffer.putInt(this.value);
//	}
	

}
