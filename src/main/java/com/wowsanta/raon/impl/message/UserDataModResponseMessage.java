package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserDataModResponseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_PS_UPDUSERDATA.getValue();
	
	CMD command   = new CMD(RaonCommand.CMD_PS_UPDUSERDATA.getValue());
	INT lot;
	
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
		int total_size = command.getSize() + lot.getSize() ;
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toBytes());
		buffer.put(lot.toBytes());
		
		this.bytes = buffer.array();
	}
}
