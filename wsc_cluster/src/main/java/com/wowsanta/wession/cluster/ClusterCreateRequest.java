package com.wowsanta.wession.cluster;

import com.wowsanta.server.Connection;
import com.wowsanta.wession.message.MessageType;
import com.wowsanta.wession.message.WessionRequest;
import com.wowsanta.wession.session.Wession;

import lombok.Data;

@Data
public class ClusterCreateRequest implements WessionRequest {
	private static final long serialVersionUID = 8243037351408871556L;
	private Wession wession;
	
	transient private Connection connection;
	
	@Override
	public MessageType getMessageType() {
		return MessageType.CREATE;
	}
	@Override
	public void parse() {
		// TODO Auto-generated method stub
		
	}

}
