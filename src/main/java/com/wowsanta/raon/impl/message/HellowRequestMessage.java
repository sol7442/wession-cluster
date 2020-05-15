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
public class HellowRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = 1167294168944095649L;
	
	CMD  command = new CMD(RaonCommand.CMD_HELLO.getValue());
	INT byteOrder;
	INT productNum;
	STR version;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		byteOrder 	= readInt(buffer);
		productNum 	= readInt(buffer);
		version    	= readStr(buffer);
	}

	@Override
	public void flush() throws IOException{
		
	}
}
