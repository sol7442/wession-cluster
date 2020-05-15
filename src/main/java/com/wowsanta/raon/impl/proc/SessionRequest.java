package com.wowsanta.raon.impl.proc;

import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.server.Message;
import com.wowsanta.server.Request;

import lombok.Data;

@Data
public class SessionRequest implements Request {
	Message message;
	public SessionRequest(RaonSessionMessage message) {
		this.message = message;
	}
}
