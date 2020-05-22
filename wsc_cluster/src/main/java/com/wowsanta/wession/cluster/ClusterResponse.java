package com.wowsanta.wession.cluster;

import com.wowsanta.server.Response;

import lombok.Data;

@Data
public class ClusterResponse implements Response {
	ClusterMessage message;
	ClusterSession session;
	public ClusterResponse(ClusterMessage message) {
		this.message = message;
	}

}
