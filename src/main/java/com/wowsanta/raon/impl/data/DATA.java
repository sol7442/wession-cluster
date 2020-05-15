package com.wowsanta.raon.impl.data;


import lombok.Data;

@Data
public abstract class DATA {
	protected int length;
	protected int padding;
	protected int size;
//	protected byte[] data;
	
	
	//protected ByteBuffer buffer;
	
	//abstract public void write(ByteBuffer buffer);
	
	abstract public byte[] toBytes();
	public int getPadding(int length) {
		int padding = 4 - length % 4;
		if(padding == 4) {
			padding = 0;
		}
		return padding;
	}
}
