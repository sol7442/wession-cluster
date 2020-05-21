package com.wowsanta.raon.impl.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class INDEX extends DATA {
	public static final int  LENGTH = 4;
	byte[] value;

	public INDEX() {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = new byte[LENGTH];
	}
	public INDEX(ByteBuffer buffer) {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = new byte[LENGTH];
		buffer.get(this.value);
	}
	public INDEX(byte[] data) {
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = data;
	}
	public void set(int idx, char data) {
		this.value[idx*2 + 1] = (byte)(data);
	}
	
	@Override
	public byte[] toBytes() {
	  	return this.value;
	}
	


}
