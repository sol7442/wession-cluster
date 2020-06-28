package com.wowsanta.raon.impl.data;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;

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
		this.value   = new byte[LENGTH];
	}
	public BYTE4(ByteBuffer buffer) throws BufferUnderflowException{
		this.length  = LENGTH;
		this.padding = 0;
		this.size    = this.length + this.padding;
		this.value   = new byte[LENGTH];
		buffer.get(this.value);
	}
	public BYTE4(byte[] data) {
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
	public static BYTE4 parse(ByteBuffer buffer) {
		BYTE4 value = null;
		try {
			buffer.mark();
			LOG.application().debug("mark : {}/{}",buffer, value);
			
			byte[] data = new byte[LENGTH];
			buffer.get(data);
			
			value = new BYTE4(data);
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
