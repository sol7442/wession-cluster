package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterResonseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_PS_REGISTER.getValue();
	
	RaonCommand command = RaonCommand.CMD_PS_REGISTER;
	
	INT createTime;
	BYTE4 sessionIndex;
	RSTRS data;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	
	
	@Override
	public void flush() throws IOException{
		int total_size = command.toCommand().getSize() + createTime.getSize() + sessionIndex.getSize() + data.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toCommand().toBytes());
		buffer.put(createTime.toBytes());
		buffer.put(sessionIndex.toBytes());
		buffer.put(data.toBytes());
		
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
