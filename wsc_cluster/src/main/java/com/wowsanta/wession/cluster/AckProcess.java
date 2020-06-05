package com.wowsanta.wession.cluster;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.message.ClusterAckRequestMessage;
import com.wowsanta.wession.message.ClusterAckResponseMessage;

public class AckProcess extends AbstractClusterProcess {

	public AckProcess(ClusterMessage message) {
		setRequest (new ClusterRequest(message));
		setResponse(new ClusterResponse(new ClusterAckResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException{
		ClusterAckRequestMessage  request_messge   = null;
		ClusterAckResponseMessage response_message = null;
		try {
			request_messge   = (ClusterAckRequestMessage) request.getMessage();
			response_message = (ClusterAckResponseMessage) response.getMessage();
			
			ClusterNode ack_node = request_messge.getNode();
			LOG.application().debug("{} ",ack_node);
			
			ClusterNode this_node = ClusterManager.getInstance().getThisNode();
					
			ClusterNode cluster_node = ClusterManager.getInstance().getClusterNode(ack_node.getName());
			
			int size = cluster_node.getNodeQueue().size();
			cluster_node.start();
			
			response_message.setNode(this_node);
			response_message.setSize(size);
			response_message.setResult(true);
		}catch (Exception e) {
			throw new ServerException(e.getMessage(),e);
		}finally {
			LOG.application().info("request  : {} ",request_messge);
			LOG.application().info("response : {} ",response_message);
		}
	}
}
