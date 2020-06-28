package com.wowsanta.raon.impl.data;


import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper=true)
public class STR extends DATA{
	String value;
	
	public STR(ByteBuffer buffer) throws ServerException,BufferUnderflowException {
		try {
				
			this.length  = buffer.getInt();
			this.padding = getPadding(length);
			this.size    = INT.LENGTH + this.length + this.padding;
			
			LOG.application().info("{}/l:{},p:{},s:{},v:{}",buffer,this.length,this.padding,this.size,this);
			
			if(this.length > buffer.remaining()) {				
				throw new ServerException("Data Header Miss : length ["+this.length+"], remain ["+buffer.remaining()+"]");
			}
			
			
			byte[] data = new byte[this.length];
			buffer.get(data);
			
			for(int i=0; i<padding; i++) {
				buffer.get();
			}
			this.value = new String(data);	
		}catch (Exception e) {
			LOG.application().warn("{}=>{}/l:{},p:{},s:{},v:{}",e.getMessage(), buffer,this.length,this.padding,this.size,this);
			buffer.clear();
			throw e;
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
	public static STR parse(ByteBuffer buffer) throws BufferUnderflowException {
		STR value = null;
		
		try {
			buffer.mark();	
			LOG.application().debug("mark : {}/{}",buffer, value);
			
			int length  = buffer.getInt();
			int padding = getPadding(length);
			
			byte[] data = new byte[length];
			buffer.get(data);
			
			for(int i=0; i<padding; i++) {
				buffer.get();
			}

			value = new STR(new String(data));
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
