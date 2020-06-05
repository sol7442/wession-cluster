package com.wowsanta.wession.cluster;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.message.ClusterPingRequestMessage;
import com.wowsanta.wession.message.ClusterPingResponseMessage;

public class PingProcess extends AbstractClusterProcess {

	public PingProcess(ClusterMessage message) {
		setRequest (new ClusterRequest(message));
		setResponse(new ClusterResponse(new ClusterPingResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException{
		ClusterPingRequestMessage  request_messge   = null;
		ClusterPingResponseMessage response_message = null;
		try {
			request_messge   = (ClusterPingRequestMessage) request.getMessage();
			response_message = (ClusterPingResponseMessage) response.getMessage();
			
			ClusterNode ping_node = request_messge.getNode();
			LOG.application().debug("{} ",ping_node);
			
			ClusterNode this_node = ClusterManager.getInstance().getThisNode();
					
			ClusterNode cluster_node = ClusterManager.getInstance().getClusterNode(ping_node.getName());
			boolean result = false;
			if(cluster_node != null) {
				result = cluster_node.wakeup();
			}
						
			int     size   = WessionCluster.getInstance().size();
			response_message.setNode(this_node);
			response_message.setResult(result);
			response_message.setSize(size);
			
		}catch (Exception e) {
			throw new ServerException(e.getMessage(),e);
		}finally {
			LOG.application().info("request  : {} ",request_messge);
			LOG.application().info("response : {} ",response_message);
		}
	}
}
