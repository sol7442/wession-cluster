package com.wowsanta.wession.cluster;


import java.io.IOException;

import com.wowsanta.server.Message;
import com.wowsanta.util.ObjectBuffer;
import com.wowsanta.wession.message.MessageType;

public abstract class ClusterMessage implements Message  {
	private static final long serialVersionUID = -6564352568546103395L;

	public abstract MessageType getMessageType();
	
	@Override
	public byte[] toBytes() throws IOException {
		return ObjectBuffer.toByteArray(this);
	}
	@Override
	public void flush() throws IOException {}
}