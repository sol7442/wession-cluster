package com.wowsanta.wession.manager;



import java.util.Set;
import java.util.Map.Entry;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.message.RegisterResponseMessage;
import com.wowsanta.wession.message.SyncResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ClusterManager extends ClusterRepository{
	private static ClusterManager instance = null;

	private NioServer clusterServer;
	
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
		try {
			registerClusterNode();
		} catch (RespositoryException e) {
			e.printStackTrace();
		}
	}
	
	public void registerClusterNode() throws RespositoryException {
		try {
			ClusterNode this_node = new ClusterNode();
			this_node.setName(this.nodeName);
			this_node.setAddress(this.clusterServer.getIpAddr());
			this_node.setPort(this.clusterServer.getPort());
			
			Set<Entry<String, ClusterNode>> node_set =  nodeMap.entrySet();
			
			ClusterNode sync_cluster = this_node;
			for (Entry<String, ClusterNode> node_entry : node_set) {
				ClusterNode cluster_node = node_entry.getValue();
				if(!nodeName.equals(cluster_node.getName()) && !cluster_node.isActive()) {
					cluster_node.initialize();
					try {
						RegisterResponseMessage response = cluster_node.register(this_node);
						if(response != null) {
							cluster_node.setActive(true);
							if(response.getSize() > sync_cluster.size()) {
								sync_cluster = cluster_node;
							}
						}else {
							cluster_node.setActive(false);
						}
						LOG.system().info("Register result : {}/{}",cluster_node.getName(),response);	
					} catch (RespositoryException e) {
						LOG.system().error(e.getMessage(), e);
					}	
				}
			}
			
			if(!sync_cluster.getName().equals(this_node.getName())) {
				SyncResponseMessage sync_response = sync_cluster.syncNode(this_node);
				LOG.system().info("Sync result : {}/{}",sync_cluster.getName(),sync_response);
			}
		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(), e);
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
	public ClusterNode getClusterNode(String name) {
		return this.nodeMap.get(name);
	}

	
}
