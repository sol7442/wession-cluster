package com.wowsanta.wession.cluster;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ServiceProcess;

import lombok.Data;

@Data
public abstract class AbstractClusterProcess implements ServiceProcess<ClusterRequest, ClusterResponse>{
	protected Connection connection;
	protected ClusterRequest 	request;
	protected ClusterResponse 	response;
}
