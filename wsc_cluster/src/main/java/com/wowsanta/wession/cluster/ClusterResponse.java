package com.wowsanta.wession.cluster;

import com.wowsanta.server.Message;
import com.wowsanta.server.Response;
import com.wowsanta.wession.message.RegisterMessage;

import lombok.Data;

@Data
public class ClusterResponse implements Response {
	Message message;
	public ClusterResponse(RegisterMessage message) {
		this.message = message;
	}

}
