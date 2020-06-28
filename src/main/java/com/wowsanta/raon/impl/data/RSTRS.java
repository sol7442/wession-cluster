package com.wowsanta.raon.impl.data;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.wowsanta.logger.LOG;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RSTRS extends DATA {
	byte[] options;
	RSTR[] values = new RSTR[4];
	
	public RSTRS() {
		this.options = new byte[4];
	}
	public RSTRS(ByteBuffer buffer) throws BufferUnderflowException{
		this.options = new byte[4];
		buffer.get(this.options);
		
		for(byte i=0; i<this.options.length; i++) {
			if(check_option(i, this.options[1])) {
				values[i] = RSTR.read(buffer);
			}
		}
	}
	
	public void add(byte idx, RSTR value) {
		this.options[1] += ((0x1)<<idx);
		values[idx] = value;
		
		this.size    = this.options.length;
		for (RSTR rstr : values) {
			if(rstr != null) {
				this.size += rstr.getSize();		
			}
		}
	}
	public void add(byte idx, String value) {
		add(idx, new RSTR((byte) idx,value));
	}
	public RSTR get(int index) {
		return this.values[index];//.get(index);
	}

	@Override
	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(this.size);
		buffer.put(this.options);
		for (RSTR rstr : values) {
			if(rstr !=null) {
				buffer.put(rstr.toBytes());				
			}
		}
		return buffer.array();
	}
	

	/**
	 * 데이터 인텍스 값 확인 -.-
	 * 0001 --> 0 번째 값이 있다.
	 * 0010 --> 0 번째 값이 없고, 1번째 있다. 
	 * 0101 --> 0 번째 값이 있고, 1번째 없고, 2번째 있다.
	 * 1111 --> 0 번째 값이 있고, 1번째 있고, 2번째 있다, 3번째 있다.
	 * @param idx
	 * @param data_opt
	 * @return
	 */
	private static boolean check_option(int idx, byte data_opt) {
		byte opt = (byte) (data_opt & ((0x1)<<idx));
		return opt != 0;
	}
	
	public static RSTRS read(ByteBuffer buffer) {
		RSTRS value = new RSTRS();
		
		byte[] data_opt  = new byte[4];
		buffer.get(data_opt);
		
		for(byte i=0; i<4; i++) {
			if(check_option(i, data_opt[1])) {
				value.add(i, RSTR.read(buffer));
			}
		}
		
		return value;
	}
	public static RSTRS parse(ByteBuffer buffer) throws BufferUnderflowException {
		RSTRS value = new RSTRS();
		try {
			buffer.mark();
			LOG.application().debug("mark : {}/{}",buffer, value);
			
			BYTE4 options = BYTE4.parse(buffer);
			
			for(byte i=0; i< options.value.length; i++) {
				if(check_option(i, options.value[1])) {
					value.add(i, RSTR.parse(buffer));
				}
			}
		}catch (BufferUnderflowException e) {
			buffer.reset();
			LOG.application().debug("reset : {}/{}",buffer, value);
			
			throw e;
		}finally {
			LOG.application().debug("parse : {}/{}",buffer, value);		
		}
		return value;
	}
	
//	@Override
//	public void write(ByteBuffer buffer) {
//		buffer.put(this.data_opt);
//		for (RSTR data : datas) {
//			data.write(buffer);
//		}
//	}

	
	
	
}
