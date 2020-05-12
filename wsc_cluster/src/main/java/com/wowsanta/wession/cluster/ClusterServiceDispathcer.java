package com.wowsanta.wession.cluster;


import java.util.Date;

import com.wowsanta.server.Request;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.message.WessionMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class ClusterServiceDispathcer extends ServiceDispatcher{
	
	@Override
	public void dispatcher(Request request) {
		try {
			WessionMessage message = (WessionMessage) request;
			switch (message.getMessageType()) {
			case REGISTER:
				RegisterRequest  register_request  = (RegisterRequest) request;
				RegisterResponse register_reponse  = (RegisterResponse) register_request.getResponse();
				register_reponse.setConnection(register_request.getConnection());
				
				ClusterNode cluster_node = new ClusterNode();
				cluster_node.setName(register_request.getName());
				cluster_node.setAddress(register_request.getAddress());
				cluster_node.setPort(register_request.getPort());
				
				ClusterManager.getInstance().addNode(cluster_node);
				
				register_reponse.setSize(100);
				register_reponse.write();
				break;
			case CREATE:
				
				break;

			default:
				break;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			log.debug("some ... : {}",new Date());
		}
	}
}
