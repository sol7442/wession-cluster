package com.wowsanta.raon.impl.proc;

import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.server.Message;
import com.wowsanta.server.Response;

import lombok.Data;

@Data
public class SessionResponse implements Response {
	Message message;
	public SessionResponse(RaonSessionMessage message) {
		this.message = message;
	}
}
