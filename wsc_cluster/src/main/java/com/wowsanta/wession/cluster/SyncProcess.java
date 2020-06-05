package com.wowsanta.wession.cluster;


import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.manager.CoreManager;
import com.wowsanta.wession.manager.SyncManager;
import com.wowsanta.wession.message.ClusterSyncRequestMessage;
import com.wowsanta.wession.message.ClusterSyncResponseMessage;

public class SyncProcess extends AbstractClusterProcess {

	public SyncProcess(ClusterMessage message) {
		setRequest (new ClusterRequest(message));
		setResponse(new ClusterResponse(new ClusterSyncResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException{
		ClusterSyncRequestMessage  request_messge   = null;
		ClusterSyncResponseMessage response_message = null;
		
		try {
			long start_time = System.currentTimeMillis();
			request_messge   = (ClusterSyncRequestMessage) request.getMessage();
			response_message = (ClusterSyncResponseMessage) response.getMessage();
			
			ClusterNode sync_node = request_messge.getNode();
			LOG.application().debug("{} ",sync_node);
			
			
			ClusterNode cluster_node = ClusterManager.getInstance().getClusterNode(sync_node.getName());		
			int sync_size = SyncManager.getInstance().sync(cluster_node);
			
			long end_time = System.currentTimeMillis();
			response_message.setNode(ClusterManager.getInstance().getThisNode());
			response_message.setTotalSize(CoreManager.getInstance().size());
			response_message.setSyncTime(end_time - start_time);
			response_message.setSyncSize(sync_size);
			
		}catch (Exception e) {
			throw new ServerException(e.getMessage(),e);
		}finally {
			
			LOG.application().info("request  : {} ",request_messge);
			LOG.application().info("response : {} ",response_message);
		}
	}
}
