package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UnkownResponseMessage extends RaonSessionMessage{
	private static final long serialVersionUID = -RaonCommand.CMD_UNKNOWN.getValue();
	
	RaonCommand command = RaonCommand.CMD_UNKNOWN;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int parse(ByteBuffer buffer) throws ServerException{
		return 0;
	}

	@Override
	public boolean isComplate() {
		return true;
	}

}
