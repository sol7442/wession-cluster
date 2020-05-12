package com.wowsanta.wession.message;


import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.wession.cluster.RegisterRequest;
import com.wowsanta.wession.cluster.RegisterResponse;

import lombok.Data;

@Data
public class RegisterMessage implements WessionMessage{
	private static final long serialVersionUID = -6548085900988010595L;
	MessageType messageType = MessageType.REGISTER;
	
	String name;
	String address;
	int port;
	
	int size;
	
	public RegisterMessage() {
		
	}
	
	@Override
	public Request getRequest() {
		return new RegisterRequest();
	}

	@Override
	public Response getResponse() {
		return new RegisterResponse();
	}
}
