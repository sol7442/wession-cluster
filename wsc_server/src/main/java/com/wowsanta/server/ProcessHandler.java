package com.wowsanta.server;

import java.io.IOException;

public interface ProcessHandler {
	public abstract int read() throws IOException;
	public abstract void run() throws ServerException;
	public abstract int write() throws IOException;
	public abstract void error(Throwable e) throws IOException;
}
