package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SearchRequestMessage extends ClusterMessage {
	private static final long serialVersionUID = 4833402542674687840L;

	MessageType messageType = MessageType.SEARCH;
	
	private String filter;
	private String orderKey;
	
	private int startIndex  = 0;
	private int count		= 0;
	
	
}
