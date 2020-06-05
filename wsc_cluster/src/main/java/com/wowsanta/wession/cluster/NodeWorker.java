package com.wowsanta.wession.cluster;

import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.Set;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.message.MessageType;
import com.wowsanta.wession.repository.RespositoryException;

public class NodeWorker  implements Runnable {
	private ClusterNode node;
	private BlockingQueue<NodeWork> nodeQueue;
	public NodeWorker(ClusterNode node, BlockingQueue<NodeWork> nodeQueue) {
		this.node = node;
	}
	public void run() {
		
		
		
		
		
//		Set<Entry<String, ClusterNode>> node_set =  ClusterManager.getInstance().getClusterNodeSet();
//		String cluster_name = ClusterManager.getInstance().getNodeName();
//		for (Entry<String, ClusterNode> node_entry : node_set) {
//			ClusterNode node = node_entry.getValue();
//			LOG.process().info("cluster.{} {} -> {}/{} ", this.type, cluster_name, node.getName(), node.isActive());
//			if(!cluster_name.equals(node.getName()) && node.isActive()) {
//				try {
//					work(node);
//				} catch (RespositoryException e) {
//					LOG.process().error(e.getMessage(), e);
//					node.setActive(false);
//				}
//			}
//		}
	}
	//public abstract void work(ClusterNode node)throws RespositoryException ;
}
