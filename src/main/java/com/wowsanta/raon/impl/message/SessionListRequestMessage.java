package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SessionListRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = RaonCommand.CMD_WRM_ACCOUNTS.getValue();
	
	RaonCommand command = RaonCommand.CMD_WRM_ACCOUNTS;
	STR userId;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public int parse(ByteBuffer buffer) throws ServerException{
		int result = -1;
		try {
			if(userId == null) {
				userId = STR.parse(buffer);
			}
			result = buffer.remaining();
		}catch (BufferUnderflowException e) {
			LOG.application().warn("{}/{}",buffer,this);
			result = -1;
		}
		return result;
	}

	@Override
	public void flush() throws IOException{
		
	}

	@Override
	public boolean isComplate() {
		return userId != null;
	}
}
