package com.wowsanta.wession.message;


import com.wowsanta.server.Message;
import com.wowsanta.server.Request;
import com.wowsanta.server.Response;

public interface WessionMessage extends Message  {
	public MessageType getMessageType();
	public Request getRequest();
	public Response getResponse();
}
