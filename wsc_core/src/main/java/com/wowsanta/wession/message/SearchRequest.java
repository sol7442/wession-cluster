package com.wowsanta.wession.message;


public class SearchRequest implements WessionRequest {
	MessageType messageType = MessageType.SEARCH;
	
	private String filter;
	private String orderKey;
	
	private int startIndex;
	private int count;
	@Override
	public MessageType getMessageType() {
		// TODO Auto-generated method stub
		return null;
	}
}
