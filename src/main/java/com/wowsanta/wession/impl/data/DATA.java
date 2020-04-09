package com.wowsanta.wession.impl.data;

import java.nio.ByteBuffer;

import lombok.Data;

@Data
public abstract class DATA {
	protected int length;
	abstract public void write(ByteBuffer buffer);
}
