package com.wowsanta.wession.cluster;

import com.wowsanta.server.Request;

import lombok.Data;

@Data
public class ClusterRequest implements Request {
	ClusterMessage message;
	ClusterSession session;
	public ClusterRequest(ClusterMessage message) {
		this.session = ClusterSession.generate();
		this.message = message;
	}
}
