package com.wowsanta.wession.impl;

import com.wowsanta.daemon.DaemonService;
import com.wowsanta.server.Server;
import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Wession extends JsonConfiguration implements DaemonService {
	
	Server server;
	
	public static void main(String[] args) {
		System.out.println("config file : " + args[0]);
		Wession wession = Wession.load(args[0],Wession.class);
		wession.initialize(null);
		wession.start();
	}

	@Override
	public boolean initialize(String config) {
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
