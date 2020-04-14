package com.wowsanta.wession.impl;

import com.wowsanta.daemon.DaemonService;
import com.wowsanta.server.Server;
import com.wowsanta.server.nio.NioServer;
import com.wowsanta.util.config.JsonConfiguration;

public class Wession implements DaemonService {

	public Server server;
	public static void main(String[] args) {
		Wession wession = new Wession();
		wession.initialize("./config/nio.server.json");
		wession.start();
	}

	@Override
	public boolean initialize(String config) {
		server = (Server) JsonConfiguration.load(config, NioServer.class);
		return server.initialize();
	}

	@Override
	public void start() {
		server.start();
	}

	@Override
	public void stop() {
		server.stop();
	}

}
