package com.wowsanta.raon.impl.session;

import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.server.Message;
import com.wowsanta.server.Response;
import com.wowsanta.server.Session;

import lombok.Data;

@Data
public class SessionResponse implements Response {
	Message message;
	Session session;
	public SessionResponse(RaonSessionMessage message, Session session) {
		this.message = message;
		this.session = session;
	}
}
