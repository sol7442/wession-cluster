package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ClusterPingRequestMessage extends ClusterMessage{
	private static final long serialVersionUID = 6046912074642144874L;
	MessageType messageType = MessageType.PING;
	ClusterNode node;
}
