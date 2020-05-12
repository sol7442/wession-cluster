package com.wowsanta.server;

import java.io.IOException;

public interface Request {
	public Connection getConnection();
	public void setConnection(Connection conn);
	
	public void read() throws IOException;
}
