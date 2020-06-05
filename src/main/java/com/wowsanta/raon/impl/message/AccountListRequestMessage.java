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
public class AccountListRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = RaonCommand.CMD_WRM_ACCOUNTS.getValue();
	
	CMD command = new CMD(RaonCommand.CMD_WRM_ACCOUNTS.getValue());
	INT page;
	INT rows;
	STR filter;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		page  = readInt(buffer);
		rows  = readInt(buffer);
		filter  = readStr(buffer);
	}

	@Override
	public void flush() throws IOException{
		
	}
}
