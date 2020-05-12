package com.wowsanta.wession.message;

import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.wession.cluster.DeleteRequest;
import com.wowsanta.wession.session.Wession;

import lombok.Data;

@Data
public class DeleteMessage implements WessionMessage {
	private static final long serialVersionUID = -5969700479523482525L;

	MessageType messageType = MessageType.DELETE;
	private Wession wession;

	public DeleteMessage(Wession wession) {
		this.wession = wession;
	}
	@Override
	public Request getRequest() {
		return new DeleteRequest(this.wession);
	}
	@Override
	public Response getResponse() {
		// TODO Auto-generated method stub
		return null;
	}
}
