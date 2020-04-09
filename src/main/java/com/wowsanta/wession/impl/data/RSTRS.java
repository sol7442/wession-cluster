package com.wowsanta.wession.impl.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RSTRS extends DATA {
	
	byte[] data_opt  = new byte[4];
	List<RSTR> datas = new ArrayList<>();

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
	
	public void add(byte idx, RSTR value) {
		this.datas.add(value);
		this.data_opt[1] += ((0x1)<<idx);
		
		this.length = 4;
		for (RSTR data : datas) {
			this.length += data.getLength();
		}
		
	}
	public void add(byte idx, String value) {
		add(idx, new RSTR((byte) idx,value));
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
	@Override
	public void write(ByteBuffer buffer) {
		buffer.put(this.data_opt);
		for (RSTR data : datas) {
			data.write(buffer);
		}
	}

	public RSTR get(int index) {
		return this.datas.get(index);
	}
	
	
	
}
