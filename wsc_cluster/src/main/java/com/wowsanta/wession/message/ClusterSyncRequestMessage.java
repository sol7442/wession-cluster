package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ClusterSyncRequestMessage extends ClusterMessage{
	private static final long serialVersionUID = 9148974208065017152L;
	MessageType messageType = MessageType.SYNC;
	ClusterNode node;
}
