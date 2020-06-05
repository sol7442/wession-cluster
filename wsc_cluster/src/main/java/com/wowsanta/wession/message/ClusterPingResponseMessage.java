package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterMessage;
import com.wowsanta.wession.cluster.ClusterNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ClusterPingResponseMessage extends ClusterMessage{
	private static final long serialVersionUID = 8184938179540929822L;

	MessageType messageType = MessageType.PING;
	ClusterNode node;
	int size;
	boolean result;
}
