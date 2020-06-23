package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SessionDelRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = RaonCommand.CMD_WRM_DELSESSION.getValue();
	
	CMD command = new CMD(RaonCommand.CMD_WRM_DELSESSION.getValue());
	BYTE4 index;
	STR userId;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		index     = readByte4(buffer);
		userId  = readStr(buffer);
	}

	@Override
	public void flush() throws IOException{
		
	}
}
