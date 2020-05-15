package com.wowsanta.wession.cluster;

import com.wowsanta.wession.message.WessionMessage;

public class UpdateProcess extends AbstractClusterProcess {

	public UpdateProcess(WessionMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() {

	}

}
