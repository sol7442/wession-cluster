package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class TokenOtpGetResponseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_PS_GETTOKENOTP.getValue();
	
	RaonCommand command = RaonCommand.CMD_PS_GETTOKENOTP;
	
	RSTRS data = new RSTRS();
	
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
		int total_size = command.toCommand().getSize() + data.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toCommand().toBytes());
		buffer.put(data.toBytes());
		
		this.bytes = buffer.array();
	}

	@Override
	public boolean isComplate() {
		// TODO Auto-generated method stub
		return false;
	}
}
