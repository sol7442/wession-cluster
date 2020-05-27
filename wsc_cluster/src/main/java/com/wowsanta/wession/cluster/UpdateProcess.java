package com.wowsanta.wession.cluster;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.manager.CoreManager;
import com.wowsanta.wession.message.UpdateMessage;

public class UpdateProcess extends AbstractClusterProcess {

	public UpdateProcess(ClusterMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			UpdateMessage request_messge   = (UpdateMessage) request.getMessage();
			LOG.application().info("request : {} ",request_messge);
			
			CoreManager.getInstance().update(request_messge.getWession());
			
		}catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}finally {
			
		}
	}

}
