package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ErrorResonseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = 1167294168944095649L;
	
	CMD command = new CMD(RaonCommand.CMD_ERROR.getValue());
	CMD request;  
	INT code;
	STR message;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		//
	}
	
	@Override
	public void flush() throws IOException{
		int total_size = command.getSize() + request.getSize() + code.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toBytes());
		buffer.put(request.toBytes());
		buffer.put(code.toBytes());
		
		this.bytes = buffer.array();
	}
}
