package com.wowsanta.server;

import java.io.IOException;

public interface Process {
	public abstract void setData(byte[] data)throws IOException;
	public abstract byte[] getData()throws IOException;
	public abstract void run() throws ServerException;
}
