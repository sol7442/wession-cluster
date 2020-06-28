package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.BufferUnderflowException;
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
public class UserDataGetResponseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_PS_GETUSERDATA.getValue();
	
	RaonCommand command = RaonCommand.CMD_PS_GETUSERDATA;
	
	INT lot;
	STR data;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	
	@Override
	public void flush() throws IOException{
		int total_size = command.toCommand().getSize() + lot.getSize() + data.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toCommand().toBytes());
		buffer.put(lot.toBytes());
		buffer.put(data.toBytes());
		
		this.bytes = buffer.array();
	}


	@Override
	public int parse(ByteBuffer buffer) throws ServerException, BufferUnderflowException {
		return 0;
	}


	@Override
	public boolean isComplate() {
		return false;
	}
}
