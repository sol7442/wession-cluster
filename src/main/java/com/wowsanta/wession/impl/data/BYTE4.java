package com.wowsanta.wession.impl.data;

import java.nio.ByteBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class BYTE4 extends DATA {
	byte[] data = new byte[4];

	public BYTE4(byte[] data) {
		this.data 	= data;
		this.length = 4;
	}
	public static BYTE4 read(ByteBuffer buffer) {
		byte[] data = new byte[4];
		buffer.get(data);
		return new BYTE4(data);
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		buffer.put(this.data);
	}
	public byte get(int idx) {
		return this.data[idx];
	}
	public void set(int idx, byte data) {
		this.data[idx] = data;
	}
}
