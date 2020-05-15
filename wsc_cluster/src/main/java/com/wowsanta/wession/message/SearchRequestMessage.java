package com.wowsanta.wession.message;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SearchRequestMessage extends WessionMessage {
	private static final long serialVersionUID = 4833402542674687840L;

	MessageType messageType = MessageType.SEARCH;
	
	private String filter;
	private String orderKey;
	
	private int startIndex  = 0;
	private int count		= 0;
	
	
}
