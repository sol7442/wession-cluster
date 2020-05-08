package com.wowsanta.server;

public class ServerException extends Exception {
	private static final long serialVersionUID = 7038722388045721948L;

	public ServerException(String msg, Exception e) {
		super(msg,e);
	}
}
