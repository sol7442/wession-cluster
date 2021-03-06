package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UnkownRequestMessage extends RaonSessionMessage{
	private static final long serialVersionUID = RaonCommand.CMD_UNKNOWN.getValue();
	
	RaonCommand command = RaonCommand.CMD_UNKNOWN;
	
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void flush() throws IOException {
		
	}

	@Override
	public int parse(ByteBuffer buffer) throws ServerException {
		int result = -1;
		try {
			
			this.bytes = buffer.array();
			
			result = buffer.remaining();
		}catch (BufferUnderflowException e) {
			LOG.application().warn("{}/{}",buffer,this);
			result = -1;
		}
		
		return result;
	}

	@Override
	public boolean isComplate() {
		return this.bytes != null;
	}

}
