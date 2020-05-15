package com.wowsanta.raon.impl.proc;

import com.wowsanta.server.Connection;
import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.server.ServiceProcess;

public abstract class AbstractSessionProcess implements ServiceProcess<Request, Response> {
	private Connection connection;
	private SessionRequest 	request;
	private SessionResponse 	response;
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public SessionRequest getRequest() {
		return request;
	}
	public void setRequest(SessionRequest request) {
		this.request = request;
	}
	public SessionResponse getResponse() {
		return response;
	}
	public void setResponse(SessionResponse response) {
		this.response = response;
	}
	
}
