package com.wowsanta.raon.impl.data;


import java.nio.ByteBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class INT extends DATA {
	public static final int  LENGTH = 4;
	int value;
	
	public INT(int value) {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = value; 
		
	}
	
	public INT(byte[] bytes, int idx) {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		
		this.value = 
				(((int)bytes[idx + 0] & 0xff) << 24) |
				(((int)bytes[idx + 1] & 0xff) << 16) |
				(((int)bytes[idx + 2] & 0xff) << 8)  |
				(((int)bytes[idx + 3] & 0xff));
	}

	public INT(long long_value) {
		this((int) (long_value/1000));
	}

	public int getValue() {
		return this.value;
	}
	
	public byte[] toBytes(){
		ByteBuffer buffer = ByteBuffer.allocate(this.size);
		buffer.putInt(this.value);
		System.out.println("this.value : " + this.value);
		

//		byte[] data = new byte[this.size];
//		
//		data[0] = (byte)(value >> 24);
//		data[1] = (byte)(value >> 16);
//		data[2] = (byte)(value >> 8);
//		data[3] = (byte)(value);
		
		return buffer.array(); 
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
//}
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
