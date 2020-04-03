package com.wowsanta.wession.impl.session;

import java.nio.ByteBuffer;

import com.wowsanta.util.Hex;

public enum RaonCommand {
	CMD_UNKNOWN(SessionServerCommand.CMD_UNKNOWN),
	CMD_HELLO(SessionServerCommand.CMD_HELLO),
	CMD_ERROR(SessionServerCommand.CMD_ERROR),
	
	CMD_PS_REGISTER(SessionServerCommand.CMD_PS_REGISTER),
	CMD_PS_SESSIONVALID(SessionServerCommand.CMD_PS_SESSIONVALID),
	CMD_PS_DELSESSION(SessionServerCommand.CMD_PS_DELSESSION)
	
	;
	
	
	private int value;
	RaonCommand(byte[] value){
		this.value = ByteBuffer.wrap(value).getInt();
	}
	public byte[] getByte() {
		return ByteBuffer.allocate(4).putInt(value).array();
	}
	public String getHexStr() {
		return Hex.toHexString(getByte());
	}

	public int getValue() {
		return this.value;
	}
	public static RaonCommand getByValue(byte[] value) {
		for (RaonCommand command : values()) {
			int int_value = ByteBuffer.wrap(value).getInt();
			if(command.value == int_value) return command;
		}
		return CMD_UNKNOWN;
	}
}
