package com.wowsanta.wession.message;

import com.wowsanta.server.Request;
import com.wowsanta.server.Response;

import lombok.Data;

@Data
public class SearchMessage implements WessionMessage {
	private static final long serialVersionUID = 4833402542674687840L;

	MessageType messageType = MessageType.SEARCH;
	
	private String filter;
	private String orderKey;
	
	private int startIndex;
	private int count;
	
	@Override
	public Request getRequest() {
		return null;
	}
	@Override
	public Response getResponse() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
