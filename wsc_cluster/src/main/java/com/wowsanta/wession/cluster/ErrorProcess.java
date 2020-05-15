package com.wowsanta.wession.cluster;

import com.wowsanta.wession.message.WessionMessage;

public class ErrorProcess extends AbstractClusterProcess {

	public ErrorProcess(WessionMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() {

	}

}
