package com.wowsanta.server;

import java.io.IOException;

public interface ProcessHandler extends Handler {
	public abstract Response read(Request request) throws IOException;
	public abstract void proc(Request request,Response response ) throws ServerException;
	public abstract int write(Response response) throws IOException;
	
	public abstract void error(Throwable e) throws IOException;
	public abstract void start() throws IOException;
	public abstract void finish() throws IOException;
}
