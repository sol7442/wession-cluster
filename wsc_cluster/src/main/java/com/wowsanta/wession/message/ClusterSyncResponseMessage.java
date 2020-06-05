package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterMessage;
import com.wowsanta.wession.cluster.ClusterNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ClusterSyncResponseMessage extends ClusterMessage{
	private static final long serialVersionUID = -5880742429460188004L;
	MessageType messageType = MessageType.SYNC;
	ClusterNode node;
	int totalSize;
	int syncSize;
	long syncTime;
}
