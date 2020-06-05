package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterMessage;
import com.wowsanta.wession.cluster.ClusterNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ClusterAckResponseMessage extends ClusterMessage{
	private static final long serialVersionUID = 9072336862641537583L;
	MessageType messageType = MessageType.ACK;
	ClusterNode node;
	int size;
	boolean result;
}
