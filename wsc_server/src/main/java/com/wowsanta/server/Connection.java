package com.wowsanta.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

public interface Connection {
	int read() throws IOException;
	void write(byte[] data)throws IOException;
}
