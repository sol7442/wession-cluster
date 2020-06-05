package com.wowsanta.wession.policy;

public class PolicyException extends Exception {
	private static final long serialVersionUID = 426463147313910023L;

	int code;
	public PolicyException(String msg) {
		super(msg);
	}

	public PolicyException(String msg, Exception e) {
		super(msg,e);
	}
	
	public PolicyException(String msg, int code) {
		super(msg);
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
