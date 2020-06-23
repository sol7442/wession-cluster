package com.wowsanta.raon.impl.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RSTRS extends DATA {
	byte[] options;
	//List<RSTR> values = new ArrayList<>();
	RSTR[] values = new RSTR[4];
	
	public RSTRS() {
		this.options = new byte[4];
	}
	public RSTRS(ByteBuffer buffer) {
		this.options = new byte[4];
		buffer.get(this.options);
		
		for(byte i=0; i<this.options.length; i++) {
			if(check_option(i, this.options[1])) {
				//values.add(i, RSTR.read(buffer));
				values[i] = RSTR.read(buffer);
			}
		}
	}
	
	public void add(byte idx, RSTR value) {
		this.options[1] += ((0x1)<<idx);
		//this.values.add(value);
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
		// if (index >= this.values.size()) {return null;}
			 
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
	
//	@Override
//	public void write(ByteBuffer buffer) {
//		buffer.put(this.data_opt);
//		for (RSTR data : datas) {
//			data.write(buffer);
//		}
//	}

	
	
	
}
