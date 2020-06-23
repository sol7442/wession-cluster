package com.wowsanta.raon.impl.data;

import java.nio.ByteBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RES_SES extends DATA {
	BYTE4 index;
	STR accessTime;
	STR createTime;
	
	public int getSize() {
		return index.getSize() + accessTime.getSize() + createTime.getSize();
	}
	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(getSize());
		
		buffer.put(index.toBytes());
		buffer.put(accessTime.toBytes());
		buffer.put(createTime.toBytes());
		
		return buffer.array();
	}
}
