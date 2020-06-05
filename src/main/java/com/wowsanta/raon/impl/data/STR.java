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
}
