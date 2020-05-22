package com.wowsanta.wession.cluster;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.manager.SyncManager;
import com.wowsanta.wession.message.RegisterRequestMessage;
import com.wowsanta.wession.message.RegisterResponseMessage;

public class RegisterProcess extends AbstractClusterProcess {

	public RegisterProcess(ClusterMessage message) {
		setRequest (new ClusterRequest(message));
		setResponse(new ClusterResponse(new RegisterResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException{
		RegisterRequestMessage  request_messge   = null;
		RegisterResponseMessage response_message = null;
		try {
			request_messge   = (RegisterRequestMessage) request.getMessage();
			response_message = (RegisterResponseMessage) response.getMessage();
			LOG.process().info("request : {} ",request_messge);
			
			int current_size = WessionCluster.getInstance().size();
			ClusterNode cluster_node = request_messge.getNode();
			
			SyncManager.getInstance().syncNode(cluster_node);

			LOG.process().debug("node : {} / {}",cluster_node, current_size);
			response_message.setSize(current_size);			
			LOG.process().info("response : {} ",response_message);
		}catch (Exception e) {
			LOG.process().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}finally {
			
		}
	}
}
