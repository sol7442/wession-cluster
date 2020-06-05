package com.wowsanta.raon.impl.session;

import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.server.Request;
import com.wowsanta.server.Session;

import lombok.Data;

@Data
public class SessionRequest implements Request {
	RaonSessionMessage message;
	Session session;
	public SessionRequest(RaonSessionMessage message) {
		this.message = message;
		this.session = SessionImp.generate();
	}
	@Override
	public Session getSession() {
		return session;
	}
}
