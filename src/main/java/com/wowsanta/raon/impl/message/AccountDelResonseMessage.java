package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class AccountDelResonseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_WRM_DELACCOUNTS.getValue();
	
	RaonCommand command = RaonCommand.CMD_WRM_DELACCOUNTS;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	
	@Override
	public void flush() throws IOException{
		int total_size = command.toCommand().getSize();

		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		buffer.put(command.toCommand().toBytes());
		
		this.bytes = buffer.array();
	}


	@Override
	public int parse(ByteBuffer buffer) throws ServerException {
		return 0;
	}


	@Override
	public boolean isComplate() {
		return false;
	}
}
