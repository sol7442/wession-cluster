package com.wowsanta.wession.cluster;

public class ErrorProcess extends AbstractClusterProcess {

	public ErrorProcess(ClusterMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() {

	}

}
