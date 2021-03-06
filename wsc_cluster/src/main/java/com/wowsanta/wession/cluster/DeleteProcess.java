package com.wowsanta.wession.cluster;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.manager.CoreManager;
import com.wowsanta.wession.manager.IndexManager;
import com.wowsanta.wession.message.DeleteMessage;

public class DeleteProcess extends AbstractClusterProcess {

	public DeleteProcess(ClusterMessage message) {
		setRequest(new ClusterRequest(message));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			DeleteMessage request_messge   = (DeleteMessage) request.getMessage();
			LOG.application().info("request : {} ",request_messge);
			
			CoreManager.getInstance().delete(request_messge.getWession());
			IndexManager.getInstance().delete(request_messge.getWession());
			
		}catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}finally {
			
		}
	}

}
