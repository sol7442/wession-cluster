package com.wowsanta.wession.impl.server;

import java.nio.ByteBuffer;

public interface RaonCommandProcessor {
	public void setRequest(ByteBuffer readBuffer);
	public byte[] getResponse();
	public boolean process();
}
