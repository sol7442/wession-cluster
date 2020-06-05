package com.wowsanta.wession.impl;


import java.util.Date;

import com.wowsanta.daemon.DaemonService;
import com.wowsanta.logger.LOG;
import com.wowsanta.server.Server;
import com.wowsanta.server.ServerException;
import com.wowsanta.util.config.JsonConfiguration;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.manager.CoreManager;
import com.wowsanta.wession.manager.IndexManager;
import com.wowsanta.wession.manager.PolicyManager;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class WessionLancher extends JsonConfiguration implements DaemonService {
	
	Server	interfaceServer;
	
	CoreManager     coreManager;
	ClusterManager  clusterManger;
	IndexManager 	indexManager;
	PolicyManager   policyManager;
	
	public static void main(String[] args) {
		String config_file = args[0];
		if(config_file == null) {
			config_file = System.getProperty("config.file");
		}
		WessionLancher lancher = WessionLancher.load(config_file);
		lancher.initialize(config_file);
		lancher.start();
	}
	@Override
	public boolean initialize(String config_file) {
		try {
			LOG.system().info("================================================================== {}", new Date());
			LOG.system().info("wowsata wession cluster edition");
			LOG.system().info("version : {} ", "v2.0.1");
			LOG.system().info("build   : {}", "2020.05.20");
			LOG.system().info("release : {}", "2020.06.10");
			LOG.system().info("@2010 wowsanta.com, all right reserved.");
			LOG.system().info("================================================================== {}", new Date());
			LOG.system().info("WessionLancher Configuration File : {} ", config_file);
			LOG.system().info("================================================================== ");
			
			if(clusterManger == null && config_file != null) {
				WessionLancher lancher = WessionLancher.load(config_file);
				
				this.clusterManger   = lancher.getClusterManger();
				this.indexManager    = lancher.getIndexManager();
				this.interfaceServer = lancher.getInterfaceServer();
				this.policyManager   = lancher.getPolicyManager();
			}
			
			coreManager = CoreManager.getInstance();
			ClusterManager.setInstance(clusterManger);
			IndexManager.setInstance(indexManager);
			PolicyManager.setInstance(policyManager);
			
			WessionCluster.getInstance().setClusterRepository(clusterManger);
			WessionCluster.getInstance().setCoreRepository(coreManager);
			WessionCluster.getInstance().setIndexRepository(indexManager);
			
			WessionCluster.getInstance().initialize();
			PolicyManager.getInstance().initialize();
			interfaceServer.initialize();
		} catch (ServerException e) {
			LOG.system().error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public void start() {
		
		try {
			WessionCluster.getInstance().start();
			interfaceServer.start();
		} catch (ServerException e) {
			LOG.system().error(e.getMessage(), e);
		}
	}

	@Override
	public void stop() {
		interfaceServer.stop();
		WessionCluster.getInstance().stop();
	}
	public static WessionLancher load(String file_name) {
		JsonConfiguration.addTypeAdapter(Server.class);
		return load(file_name,WessionLancher.class);
	}
}
