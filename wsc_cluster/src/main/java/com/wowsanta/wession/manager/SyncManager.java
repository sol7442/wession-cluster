package com.wowsanta.wession.manager;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.session.Wession;

public class SyncManager {
	private static SyncManager instance = null;
	public static SyncManager getInstance() {
		if(instance == null) {
			instance = new SyncManager();
		}
		return instance;
	}
	
	ExecutorService syncService = Executors.newSingleThreadExecutor();
	public void syncNode(ClusterNode node) {
		syncService.submit(new SyncWorker(node.getName()));	
	}
	
	public class SyncWorker implements Callable<Integer>{
		int before_size = 0;
		int sync_size   = 0;
		int after_size  = 0;
		
		String nodeName;
		public SyncWorker(String node_name) {
			this.nodeName = node_name;
			this.before_size = CoreManager.getInstance().size();
		}
		@Override
		public Integer call() throws Exception {
			
			try {
				ClusterNode sync_node = ClusterManager.getInstance().getClusterNode(this.nodeName);
				List<Wession> sync_lilst = CoreManager.getInstance().list();
				before_size = sync_lilst.size();
				for (Wession wession : sync_lilst) {
					sync_node.sync(wession);
					sync_size++;
				}
				//ClusterManager.getInstance().setClusterNode(this.node);	
			}catch (Exception e) {
				LOG.process().error(e.getMessage(), e);
			}finally {
				after_size = CoreManager.getInstance().size();;
				LOG.process().info("{}:{}/{}/{}", this.nodeName, this.before_size,this.sync_size,this.after_size);
			}
			return sync_size;
		}
	}
}
