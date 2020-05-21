package com.wowsanta.wession.cluster;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.manager.CoreManager;
import com.wowsanta.wession.manager.IndexManager;
import com.wowsanta.wession.message.CreateMessage;
import com.wowsanta.wession.message.WessionMessage;

public class CreateProcess extends AbstractClusterProcess {

	public CreateProcess(WessionMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			CreateMessage request_messge   = (CreateMessage) request.getMessage();
			LOG.process().info("request : {} ",request_messge);
			
			CoreManager.getInstance().create(request_messge.getWession());
			IndexManager.getInstance().create(request_messge.getWession());
			
			
		}catch (Exception e) {
			LOG.process().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}finally {
			
		}
	}
}
