package com.wowsanta.raon.impl.data;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RSTR extends DATA {
	byte index;
	String value;
	public RSTR(ByteBuffer buffer) {
		this.length  = buffer.getInt();
		this.index   = buffer.get();
		
		byte[] byte_data = new byte[this.length];
		buffer.get(byte_data);
		
		this.padding = getPadding(length + 1);
		for(int i=0; i<padding; i++) {
			buffer.get();
		}
		
		this.size    = INT.LENGTH + 1 + this.length + this.padding;
		this.value   = new String(byte_data);
	}
	public RSTR(byte idx, String str) throws BufferUnderflowException{
		byte[] byte_data = str.getBytes();
		
		this.length  = byte_data.length;
		this.padding = getPadding(length + 1);
		
		this.size    = INT.LENGTH + 1 + this.length + this.padding;
		this.value   = str;
		this.index   = idx;
	}
	
	public RSTR(byte idx, byte[] byte_data) {
		
		this.length  = byte_data.length;
		this.padding = getPadding(length + 1);
		
		this.size    = INT.LENGTH + 1 + this.length + this.padding;
		this.value   = new String(byte_data);
		this.index   = idx;
	}
	
	@Override
	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(this.size);
		buffer.putInt(value.length() + 1);
		buffer.put(this.index);
		buffer.put(this.value.getBytes());
		
		for(int i=0; i<padding; i++) {
			buffer.put((byte) 0x00);
		}
		return buffer.array();
	}
	
	
	public static RSTR read(ByteBuffer buffer) throws BufferUnderflowException{
		int data_len = buffer.getInt();
		byte idx = buffer.get();
		int padding = 4 - data_len%4;
		if(padding == 4) {
			padding = 0;
		}
		
		byte[] str_data = new byte[data_len - 1];
		buffer.get(str_data);
		while(padding > 0) {
			buffer.get();
			padding--;
		}
		return new RSTR(idx, new String(str_data));
	}
	
	public void write(ByteBuffer buffer) {
		int data_len = this.value.length() + 1;
		int padding = 4 - data_len%4;
		if(padding == 4) {
			padding = 0;
		}
		
		buffer.putInt(data_len);
		buffer.put(this.index);
		buffer.put(value.getBytes());
		for(int i=0; i<padding; i++) {
			buffer.put((byte) 0x00);
		}
	}
	public static RSTR parse(ByteBuffer buffer) {
		RSTR value = null;
		try {
			buffer.mark(); 
			LOG.application().debug("mark : {}/{}",buffer, value);
			
			INT length = new INT(buffer.getInt());
			byte index = buffer.get();
			
			byte[] byte_data = new byte[length.getValue() - 1];
			buffer.get(byte_data);
			
			int padding = getPadding(length.getValue());
			for(int i=0; i<padding; i++) {
				buffer.get();
			}
			value = new RSTR(index, byte_data);
			
			
		}catch (BufferUnderflowException e) {
			buffer.reset();
			LOG.application().debug("reset : {}/{}",buffer, value);
			
			throw e;
		}finally {
			LOG.application().debug("parse : {}/{}",buffer, value);			
		}
		
		return value;
	}

	
}
