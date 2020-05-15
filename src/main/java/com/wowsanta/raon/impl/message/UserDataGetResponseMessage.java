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
public class UserDataGetResponseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_PS_GETUSERDATA.getValue();
	
	CMD command   = new CMD(RaonCommand.CMD_PS_GETUSERDATA.getValue());
	INT lot;
	STR data;
	
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
		int total_size = command.getSize() + lot.getSize() + data.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toBytes());
		buffer.put(lot.toBytes());
		buffer.put(data.toBytes());
		
		this.bytes = buffer.array();
	}
}
