package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SessionListRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = RaonCommand.CMD_WRM_ACCOUNTS.getValue();
	
	CMD command = new CMD(RaonCommand.CMD_WRM_ACCOUNTS.getValue());
	STR userId;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		userId  = readStr(buffer);
	}

	@Override
	public void flush() throws IOException{
		
	}
}
