package com.wowsanta.wession.cluster;

import com.wowsanta.server.ServerException;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.message.RegisterMessage;
import com.wowsanta.wession.message.WessionMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterProcess extends AbstractClusterProcess {

	public RegisterProcess(WessionMessage message) {
		setRequest (new ClusterRequest(message));
		setResponse(new ClusterResponse(new RegisterMessage()));
	}

	@Override
	public void porcess() throws ServerException{
		
		try {
			RegisterMessage request_messge   = (RegisterMessage) request.getMessage();
			RegisterMessage response_message = (RegisterMessage) response.getMessage();
			
			ClusterNode cluster_node = new ClusterNode();
			cluster_node.setName(request_messge.getName());
			cluster_node.setAddress(request_messge.getAddress());
			cluster_node.setPort(request_messge.getPort());
			
			ClusterManager.getInstance().addNode(cluster_node);
			
			response_message.setSize(100);
			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}
}
