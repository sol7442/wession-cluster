package com.wowsanta.wession.cluster;

import com.wowsanta.wession.message.WessionMessage;

public class DeleteProcess extends AbstractClusterProcess {

	public DeleteProcess(WessionMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() {

	}

}
