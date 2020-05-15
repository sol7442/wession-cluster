package com.wowsanta.raon.impl.data;

import java.nio.ByteBuffer;

import com.wowsanta.server.Response;

public abstract class RaonSessionResponse implements Response{
	protected ByteBuffer buffer;
	public void allocate(int size) {
		buffer = ByteBuffer.allocate(size);
	}
	public void writeCommand(int value) {
		buffer.putInt(value);
	}
	public void writeInt(INT value) {
		buffer.putInt(value.value);
	}
	public void writeStr(STR str) {
		buffer.putInt(str.getLength());
		buffer.put(str.getValue().getBytes());
		for(int i=0; i<str.getPadding(); i++) {
			buffer.put((byte) 0x00);
		}
	}
}
