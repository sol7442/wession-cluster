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
public class ErrorResonseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = 1167294168944095649L;
	
	RaonCommand command = RaonCommand.CMD_ERROR;
	
	CMD request;  
	INT code;
	STR message;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public int parse(ByteBuffer buffer) throws ServerException {
		return 0;
	}
	
	@Override
	public void flush() throws IOException{
		int total_size = command.toCommand().getSize() + request.getSize() + code.getSize() + message.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toCommand().toBytes());
		buffer.put(request.toBytes());
		buffer.put(code.toBytes());
		buffer.put(message.toBytes());
		
		this.bytes = buffer.array();
	}

	@Override
	public boolean isComplate() {
		return false;
	}
}
