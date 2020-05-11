package com.wowsanta.wession.message;

import com.wowsanta.server.Connection;

import lombok.Data;

@Data
public class SearchRequest implements WessionRequest {
	private static final long serialVersionUID = 1L;

	MessageType messageType = MessageType.SEARCH;
	
	private String filter;
	private String orderKey;
	
	private int startIndex;
	private int count;
	
	transient private Connection connection;
	
	@Override
	public void parse() {
		// TODO Auto-generated method stub
		
	}
}
