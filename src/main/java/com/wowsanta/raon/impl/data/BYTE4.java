package com.wowsanta.raon.impl.data;

import java.nio.ByteBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class BYTE4 extends DATA {
	public static final int  LENGTH = 4;
	byte[] value;

	public BYTE4() {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = new byte[length];
	}
	public BYTE4(ByteBuffer buffer) {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = new byte[length];
		buffer.get(value);
	}
	
	public BYTE4(byte[] data) {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = new byte[length];
		
		System.arraycopy(data,0,this.value,0,LENGTH);
	}
	

	public byte get(int idx) {
		return this.value[idx];
	}
	public void set(int idx, byte data) {
		this.value[idx] = data;
	}

	@Override
	public byte[] toBytes() {
		return this.value;
	}
	
	
//	public static BYTE4 read(ByteBuffer buffer) {
//		byte[] data = new byte[4];
//		buffer.get(data);
//		return new BYTE4(data);
//	}
//	
//	@Override
//	public void write(ByteBuffer buffer) {
//		buffer.put(this.data);
//	}
	
}
