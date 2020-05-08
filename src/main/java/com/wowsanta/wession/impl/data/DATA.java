package com.wowsanta.wession.impl.data;


import lombok.Data;

@Data
public abstract class DATA {
	protected int length;
	//abstract public byte[] toBytes();
	//abstract public void write(ByteBuffer buffer);
	
	public int getPadding(int length) {
		int padding = 4 - length % 4;
		if(padding == 4) {
			padding = 0;
		}
		return padding;
	}
}
