package com.wowsanta.server;


public interface Response {
	public Connection getConnection();
	public void setConnection(Connection conn);
	public void write() throws ServerException;
}
