package com.wowsanta.wession.manager;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.message.RegisterMessage;
import com.wowsanta.wession.repository.RespositoryException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class ClusterManager extends ClusterRepository{
	private static ClusterManager instance = null;

	private transient final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private transient final Lock readLock = readWriteLock.readLock();
	private transient final Lock writeLock = readWriteLock.writeLock();

	private transient boolean initialized = false;
	
	private NioServer clusterServer;
	private Set<ClusterNode> nodes = new HashSet<>();

	
	public static ClusterManager getInstance() {
		if(instance == null) {
			instance = new ClusterManager();
		}
		return instance;
	}
	
	public boolean initialize() {
		if(initialized == false) {
			clusterServer.initialize();
			this.clusterServer.start();
			
			for (ClusterNode cluster_node : nodes) {
				cluster_node.initialize();
				try {
					
					RegisterMessage message = cluster_node.register();
					
					log.info("register result : {}",message);
				} catch (RespositoryException e) {
					log.error(e.getMessage(), e);
					removeNode(cluster_node.getName());
				}
			}
			
			initialized = true;
		}
		return initialized;
	}

	@Override
	public ClusterNode[] getNodeArray() {
		ClusterNode[] node_array = null;
		readLock.lock();
		try {
			node_array = new ClusterNode[nodes.size()] ;
			return nodes.toArray(node_array);
		}finally {
			readLock.unlock();
		}
	}
	public void removeNode(String name) {
		writeLock.lock();
		try {
			for (ClusterNode node : nodes) {
				if(node.getName().equals(name)) {
					nodes.remove(node);
					log.debug("removed cluster node : {} ", nodes);
				}
			}
		}finally {
			writeLock.unlock();
		}
	}
	public void addNode(ClusterNode node) {
		writeLock.lock();
		try {
			this.nodes.add(node);
			log.debug("add cluster node : {} ", nodes);
		}finally {
			writeLock.unlock();
		}
	}
	
}
