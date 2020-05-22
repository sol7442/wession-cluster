package com.wowsanta.wession.manager;



import java.util.Set;
import java.util.Map.Entry;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.message.RegisterResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ClusterManager extends ClusterRepository{
	private static ClusterManager instance = null;

	private NioServer clusterServer;
	private String nodeName;
	
	public static ClusterManager getInstance() {
		if(instance == null) {
			instance = new ClusterManager();
		}
		return instance;
	}
	public static void setInstance(ClusterManager clusterManger) {
		if(instance == null) {
			instance = clusterManger;
		}
	}
	public boolean initialize()  {
		boolean initialized = false;
		try {
			initialized = this.clusterServer.initialize();
			initialized = super.initialize();
		} catch (ServerException e) {
			LOG.system().error(e.getMessage(), e);
			initialized = false;	
		}finally {
			LOG.system().info("initialized : {} ", initialized);
		}
		return initialized;
	}
	public void start() throws ServerException {
		this.clusterServer.start();	
		
		registerClusterNode();	
	}
	private void registerClusterNode() {
		ClusterNode this_node = new ClusterNode();
		this_node.setName(this.nodeName);
		this_node.setAddress(this.clusterServer.getIpAddr());
		this_node.setPort(this.clusterServer.getPort());
		
		Set<Entry<String, ClusterNode>> node_set =  nodeMap.entrySet();
		for (Entry<String, ClusterNode> node_entry : node_set) {
			ClusterNode cluster_node = node_entry.getValue();
			cluster_node.initialize();
			try {
				RegisterResponseMessage message = cluster_node.register(this_node);
				if(message != null) {
					cluster_node.setActive(true); 
				}else {
					cluster_node.setActive(false);
				}
				LOG.system().info("Register result : {}/{}",cluster_node.getName(),message);
			} catch (RespositoryException e) {
				LOG.system().error(e.getMessage(), e);
			}
		}
	}
	public void stop() {
		this.clusterServer.stop();
	}
	
	public void setClusterNode(ClusterNode node) {
		LOG.system().debug("{}",nodeMap);
		
		ClusterNode cluster_node = nodeMap.get(node.getName());
		LOG.system().debug("{}/{}",node.getName(),cluster_node);
		if(cluster_node == null) {
			cluster_node = node;
			nodeMap.put(cluster_node.getName(),cluster_node);
		}
		cluster_node.initialize();
		cluster_node.setActive(true);
		LOG.system().info("{}/{}",node.getName(),cluster_node);
	}

	
}
