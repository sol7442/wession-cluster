package com.wowsanta.wession.message;

import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.wession.cluster.UpdateRequest;
import com.wowsanta.wession.session.Wession;

import lombok.Data;

@Data
public class UpdateMessage implements WessionMessage {
	private static final long serialVersionUID = 7428925535426149542L;
	MessageType messageType = MessageType.UPDATE;
	private Wession wession;
	
	public UpdateMessage(Wession wession) {
		this.wession = wession;
	}

	@Override
	public Request getRequest() {
		return new UpdateRequest(this.wession);
	}

	@Override
	public Response getResponse() {
		// TODO Auto-generated method stub
		return null;
	}
}
