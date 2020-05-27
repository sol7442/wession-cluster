package com.wowsanta.wession.cluster;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.manager.SyncManager;
import com.wowsanta.wession.message.RegisterRequestMessage;
import com.wowsanta.wession.message.RegisterResponseMessage;
import com.wowsanta.wession.message.SyncRequestMessage;
import com.wowsanta.wession.message.SyncResponseMessage;

public class SyncProcess extends AbstractClusterProcess {

	public SyncProcess(ClusterMessage message) {
		setRequest (new ClusterRequest(message));
		setResponse(new ClusterResponse(new SyncResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException{
		SyncRequestMessage  request_messge   = null;
		SyncResponseMessage response_message = null;
		try {
			request_messge   = (SyncRequestMessage) request.getMessage();
			response_message = (SyncResponseMessage) response.getMessage();
			LOG.application().info("request : {} ",request_messge);
			
			int current_size = WessionCluster.getInstance().size();
			ClusterNode cluster_node = request_messge.getNode();
			
			SyncManager.getInstance().syncNode(cluster_node);
			
			LOG.application().debug("node : {} / {}",cluster_node, current_size);
			response_message.setSize(current_size);			
			LOG.application().info("response : {} ",response_message);
			
		}catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}finally {
			
		}
	}
}
