package com.wowsanta.wession.message;


import com.wowsanta.server.Request;

public interface WessionRequest extends Request {
	public MessageType getMessageType();
}
