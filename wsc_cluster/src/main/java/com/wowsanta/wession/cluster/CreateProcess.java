package com.wowsanta.wession.cluster;

import com.wowsanta.wession.message.WessionMessage;

public class CreateProcess extends AbstractClusterProcess {

	public CreateProcess(WessionMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() {

	}

}
