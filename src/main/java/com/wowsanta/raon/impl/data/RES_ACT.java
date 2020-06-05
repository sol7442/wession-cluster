package com.wowsanta.raon.impl.data;

import java.nio.ByteBuffer;

import lombok.Data;
import lombok.EqualsAndHashCode;

// resource - accouint
@Data
@EqualsAndHashCode(callSuper=true)
public class RES_ACT extends DATA {
	STR userId;
	INT sessionCount;
	STR accessTime;
	STR createTime;
	
	public int getSize() {
		return userId.getSize() + INT.LENGTH + accessTime.getSize() + createTime.getSize();
	}
	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(getSize());
		
		buffer.put(userId.toBytes());
		buffer.put(sessionCount.toBytes());
		buffer.put(accessTime.toBytes());
		buffer.put(createTime.toBytes());
		
		return buffer.array();
	}
}
