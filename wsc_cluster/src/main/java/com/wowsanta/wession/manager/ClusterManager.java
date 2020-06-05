package com.wowsanta.wession.manager;



import java.util.Set;
import java.util.Map.Entry;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.message.ClusterAckResponseMessage;
import com.wowsanta.wession.message.ClusterPingResponseMessage;
import com.wowsanta.wession.message.ClusterSyncResponseMessage;
import com.wowsanta.wession.message.MessageType;
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
			if(this.clusterServer.initialize()) {
				initialized = super.initialize();
			}
		} catch (ServerException e) {
			LOG.system().error(e.getMessage(), e);
			initialized = false;	
		}finally {
			LOG.system().info("initialized : {}-{} ", this.getClass().getName(), initialized);
		}
		return initialized;
	}
	public void start() throws ServerException {
		this.clusterServer.start();	
		
		ClusterNode sync_cluster =  ping();
		if(sync_cluster != null) {
			sync(sync_cluster);
		}
		wakeup();
	}
	private void wakeup() {
		ClusterNode this_node = getThisNode();
		
		Set<Entry<String, ClusterNode>> node_set = getClusterNodeSet();
		for (Entry<String, ClusterNode> node_entry : node_set) {
			ClusterNode cluster_node = node_entry.getValue();
			if(!nodeName.equals(cluster_node.getName()) && cluster_node.isActived()) {
				ClusterAckResponseMessage response = null;
				boolean result  = false;
				try {
					response = cluster_node.ack(this_node);
					if(response != null) {
						cluster_node.start();
						result = true;
					}
				} catch (RespositoryException e) {
					LOG.system().warn(e.getMessage());
				}finally {
					LOG.system().info("{} -> {} : wakeup({})", cluster_node.getName(), this_node.getName(), result);	
				}
			}
		}
	}
	public void sync(ClusterNode sync_cluster) throws ServerException{
		try {
			ClusterNode this_node = getThisNode();
			ClusterSyncResponseMessage response = sync_cluster.sync(this_node);
			LOG.system().info("sync {}->{} : {}/{} [{}]",sync_cluster.getName(),this_node.getName(),response.getSyncSize(),response.getTotalSize(),response.getSyncTime() );
		} catch (RespositoryException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	private ClusterNode ping() {
		ClusterNode this_node = getThisNode();
		
		ClusterNode sync_cluster = null;
		int max_size = 0;
		Set<Entry<String, ClusterNode>> node_set = getClusterNodeSet();
		for (Entry<String, ClusterNode> node_entry : node_set) {
			
			ClusterNode cluster_node = node_entry.getValue();
			if(!nodeName.equals(cluster_node.getName())) {
				ClusterPingResponseMessage response = null;
				boolean result = false;
				try {
					response = cluster_node.ping(this_node);
					if(response != null) {
						if(max_size < response.getSize()) {
							max_size = response.getSize();
							sync_cluster = cluster_node;
						}
						result = cluster_node.wakeup();
					}
				} catch (RespositoryException e) {
					LOG.system().warn(e.getMessage());
				}finally {
					LOG.system().info("{} -> {} : wakeup({})",this_node.getName() , cluster_node.getName(), result );	
				}
			}
		}
		return sync_cluster;
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
		LOG.system().info("{}/{}",node.getName(),cluster_node);
	}
	public ClusterNode getClusterNode(String name) {
		return this.nodeMap.get(name);
	}
	public ClusterNode getThisNode() {
		ClusterNode this_node = new ClusterNode();
		this_node.setName(this.nodeName);
		this_node.setAddress(this.clusterServer.getIpAddr());
		this_node.setPort(this.clusterServer.getPort());
		return this_node;
	}
	
}
