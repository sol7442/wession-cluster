package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ClusterAckRequestMessage extends ClusterMessage{
	private static final long serialVersionUID = -4147361274283001555L;
	MessageType messageType = MessageType.ACK;
	ClusterNode node;
}
