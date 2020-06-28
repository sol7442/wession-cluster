package com.wowsanta.raon.impl.data;


import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;

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
		
		return buffer.array(); 
	}

	public static INT parse(ByteBuffer buffer) throws BufferUnderflowException {
		INT value = null;
		buffer.mark(); 
		try {			
			LOG.application().debug("mark : {}/{}",buffer, value);
			
			value = new INT(buffer.getInt());
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
