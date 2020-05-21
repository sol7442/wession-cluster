package com.wowsanta.wession.cluster;

import com.wowsanta.server.Message;
import com.wowsanta.server.Response;

import lombok.Data;

@Data
public class ClusterResponse implements Response {
	Message message;
	public ClusterResponse(Message message) {
		this.message = message;
	}

}
