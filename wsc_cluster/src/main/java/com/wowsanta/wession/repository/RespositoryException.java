package com.wowsanta.wession.repository;

public class RespositoryException extends Exception {

	private static final long serialVersionUID = 1L;

	
	public RespositoryException(String msg) {
		super(msg);
	}


	public RespositoryException(String msg, Exception e) {
		super(msg,e);
	}
}
