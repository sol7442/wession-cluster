package com.wowsanta.wession.message;

import lombok.Data;

@Data
public class SearchRequest implements WessionRequest {
	MessageType messageType = MessageType.SEARCH;
	
	private String filter;
	private String orderKey;
	
	private int startIndex;
	private int count;
}
