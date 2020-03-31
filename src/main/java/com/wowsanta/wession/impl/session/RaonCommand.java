package com.wowsanta.wession.impl.session;

import java.nio.ByteBuffer;

public enum RaonCommand {
	CMD_HELLO(SessionServerCommand.CMD_HELLO),
	CMD_ERROR(SessionServerCommand.CMD_ERROR);
	
	private int command;
	RaonCommand(byte[] value){
		command = ByteBuffer.wrap(value).getInt();
	}
	public int Command() {
		return this.command;
	}
}
