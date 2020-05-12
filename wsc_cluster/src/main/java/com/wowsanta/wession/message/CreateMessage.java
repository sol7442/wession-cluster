package com.wowsanta.wession.message;

import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.wession.cluster.CreateRequest;
import com.wowsanta.wession.session.Wession;

import lombok.Data;

@Data
public class CreateMessage implements WessionMessage {
	private static final long serialVersionUID = -6548085900988010595L;
	MessageType messageType = MessageType.CREATE;
	private Wession wession;
	
	public CreateMessage(Wession wession) {
		this.wession = wession;
	}

	@Override
	public Request getRequest() {
		return new CreateRequest(this.wession);
	}

	@Override
	public Response getResponse() {
		return null;
	}
}
