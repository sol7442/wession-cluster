package com.wowsanta.server;


public interface ServiceProcess<T extends Request, S extends Response> {
	
	public void porcess() throws ServerException;
	public T getRequest();
	public S getResponse();
	public void setConnection(Connection connection);
	public Connection getConnection();
	
}
