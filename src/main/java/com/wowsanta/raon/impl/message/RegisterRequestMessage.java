package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = RaonCommand.CMD_PS_REGISTER.getValue();
	
	CMD command = new CMD(RaonCommand.CMD_PS_REGISTER.getValue());
	BYTE4 option;
	STR userId;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		option 	= readByte4(buffer);
		userId  = readStr(buffer);
	}

	@Override
	public void flush() throws IOException{
		
	}
}
