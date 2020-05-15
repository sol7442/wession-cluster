package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserDataAddRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = RaonCommand.CMD_PS_ADDUSERDATA.getValue();
	
	CMD command = new CMD(RaonCommand.CMD_PS_ADDUSERDATA.getValue());
	STR userId;
	BYTE4 sessionIndex;
	STR data;
	
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		userId  = readStr(buffer);
		sessionIndex 	= readByte4(buffer);
		data    = readStr(buffer);
	}

	@Override
	public void flush() throws IOException{
		
	}
}
