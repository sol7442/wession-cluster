package com.wowsanta.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

public interface Connection {
	int read0() throws ServerException;
	int write0() throws ServerException;
//	
//	int read(byte[] date) throws IOException;
//	void write(byte[] data)throws IOException;
}
