package com.wowsanta.wession.impl;

import com.wowsanta.daemon.DaemonService;
import com.wowsanta.server.ConnectionFactory;
import com.wowsanta.server.Server;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.util.config.JsonConfiguration;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.impl.server.RaonInterfaceServer;
import com.wowsanta.wession.index.IndexRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;


@Data
@EqualsAndHashCode(callSuper=false)
@Slf4j
public class WessionLancher extends JsonConfiguration implements DaemonService {
	static {
		JsonConfiguration.addTypeAdapter(Server.class);
	}
	
	
	Server	interfaceServer;
	Server   clusterServer;
	IndexRepository     indexService;
	
	public static void main(String[] args) {
		log.info("config file : {} ", args[0]);

		WessionLancher lancher = WessionLancher.load(args[0],WessionLancher.class);
		lancher.initialize(null);
		lancher.start();
		
	}
	@Override
	public boolean initialize(String config) {

		interfaceServer.initialize();
		clusterServer.initialize();
		indexService.initialize();
		
		return true;
	}

	@Override
	public void start() {
		interfaceServer.start();
		clusterServer.start();
	}

	@Override
	public void stop() {
		clusterServer.stop();
		interfaceServer.stop();
	}
}
