package com.wowsanta.wession.impl;


import java.util.Date;

import com.wowsanta.daemon.DaemonService;
import com.wowsanta.logger.LOG;
import com.wowsanta.server.Server;
import com.wowsanta.util.config.JsonConfiguration;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.manager.CoreManager;
import com.wowsanta.wession.manager.IndexManager;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class WessionLancher extends JsonConfiguration implements DaemonService {
	
	static {
		JsonConfiguration.addTypeAdapter(Server.class);
	}
	
	Server	interfaceServer;
	
	CoreManager     coreManager;
	ClusterManager  clusterManger;
	IndexManager 	indexManager;
	
	public static void main(String[] args) {
		String config_file = args[0];
		LOG.system().info("================================================================== ");
		LOG.system().info("wowsata wession cluster edition");
		LOG.system().info("version : {} ", "v2.0.1");
		LOG.system().info("build   : {}", "2020.05.20");
		LOG.system().info("release : {}", "2020.06.10");
		LOG.system().info("@2010 wowsanta.com, all right reserved.");
		LOG.system().info("================================================================== {}", new Date());
		LOG.system().info("WessionLancher Configuration File : {} ", config_file);
		WessionLancher lancher = WessionLancher.load(config_file,WessionLancher.class);
		lancher.initialize(null);
		lancher.start();
		LOG.system().info("================================================================== {}", config_file);
	}
	@Override
	public boolean initialize(String config) {
		coreManager = CoreManager.getInstance();
		ClusterManager.setInstance(clusterManger);
		IndexManager.setInstance(indexManager);
		
		WessionCluster.getInstance().setClusterRepository(clusterManger);
		WessionCluster.getInstance().setCoreRepository(coreManager);
		WessionCluster.getInstance().setIndexRepository(indexManager);

		interfaceServer.initialize();
		return true;
	}

	@Override
	public void start() {
		clusterManger.start();
		interfaceServer.start();
	}

	@Override
	public void stop() {
		interfaceServer.stop();
		clusterManger.stop();
	}
}
