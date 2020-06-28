package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class HelloResonseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_HELLO.getValue();
	
	RaonCommand command = RaonCommand.CMD_HELLO;

	
	INT byteOrder = new INT(1);
	INT code ;     
	STR version = new STR("WOWSANTA WESSION - WITH WISEACCESS : b2020.04");
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	
	@Override
	public void flush() throws IOException{
		int total_size = command.toCommand().getSize() + byteOrder.getSize() + code.getSize() + version.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toCommand().toBytes());
		buffer.put(byteOrder.toBytes());
		buffer.put(code.toBytes());
		buffer.put(version.toBytes());
		
		this.bytes = buffer.array();
	}

	@Override
	public boolean isComplate() {
		return false;
	}
	@Override
	public int parse(ByteBuffer buffer) throws ServerException {
		return 0;
	}
}
