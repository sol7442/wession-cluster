package com.wowsanta.wession.cluster;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ClusterRepository implements WessionRepository<Wession> {
	transient protected BlockingQueue<ClusterMessage> requestQueue ;
	
//	protected transient ThreadPoolExecutor executor;
	private transient boolean initialized = false;
	
	protected ConcurrentMap<String, ClusterNode> nodeMap = new ConcurrentHashMap<>();
	protected int threadCount;
	protected String nodeName;

	public boolean initialize()  {
		if(initialized == false) {
			Set<Entry<String, ClusterNode>> node_set =  nodeMap.entrySet();
			for (Entry<String, ClusterNode> entry : node_set) {
				ClusterNode cluster_node = entry.getValue();
				if(!nodeName.equals(cluster_node.getName())) {
					cluster_node.initialize();
				}
			}
			initialized = true;			
		}
		
		
		return true;
	}
	
	@Override
	public void create(Wession session) throws RespositoryException {
		LOG.process().debug("create.cluster : {} ", session.getKey());
		Set<Entry<String, ClusterNode>> node_set = getClusterNodeSet();
		for (Entry<String, ClusterNode> cluster_entry : node_set) {
			ClusterNode cluster_node = cluster_entry.getValue();
			if(!nodeName.equals(cluster_node.getName()) && cluster_node.isActived()){
				cluster_node.create(session);
			}
		}		
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}

	@Override
	public void update(Wession session) throws RespositoryException {
		LOG.process().debug("update : {} ", session.getKey());
		Set<Entry<String, ClusterNode>> node_set = getClusterNodeSet();
		for (Entry<String, ClusterNode> cluster_entry : node_set) {
			ClusterNode cluster_node = cluster_entry.getValue();
			if(!nodeName.equals(cluster_node.getName()) && cluster_node.isActived()){
				cluster_node.update(session);
			}
		}
	}

	@Override
	public void delete(Wession session) throws RespositoryException {
		LOG.process().debug("delete : {} ", session.getKey());
		Set<Entry<String, ClusterNode>> node_set = getClusterNodeSet();
		for (Entry<String, ClusterNode> cluster_entry : node_set) {
			ClusterNode cluster_node = cluster_entry.getValue();
			if(!nodeName.equals(cluster_node.getName()) && cluster_node.isActived()){
				cluster_node.delete(session);
			}
		}
	}

	@Override
	public SearchResponseMessage search(SearchRequestMessage r)throws RespositoryException{
		return null;
	}
	@Override
	public int size() {
		return 0;
	}
	public Set<Entry<String, ClusterNode>> getClusterNodeSet() {
		return this.nodeMap.entrySet();
	}
}
