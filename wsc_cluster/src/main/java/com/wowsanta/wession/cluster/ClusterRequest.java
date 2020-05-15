package com.wowsanta.wession.cluster;

import com.wowsanta.server.Message;
import com.wowsanta.server.Request;
import com.wowsanta.wession.message.WessionMessage;

import lombok.Data;

@Data
public class ClusterRequest implements Request {
	Message message;
	public ClusterRequest(WessionMessage message) {
		this.message = message;
	}
}
