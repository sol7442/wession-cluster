package com.wowsanta.wession.message;


import java.io.IOException;

import com.wowsanta.server.Message;
import com.wowsanta.util.ObjectBuffer;

public abstract class WessionMessage implements Message  {
	private static final long serialVersionUID = -6564352568546103395L;

	public abstract MessageType getMessageType();
	
	@Override
	public byte[] toBytes() throws IOException {
		return ObjectBuffer.toByteArray(this);
	}
	@Override
	public void flush() throws IOException {}
}