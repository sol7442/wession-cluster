package com.wowsanta.wession.impl.server;

import com.wowsanta.server.nio.NioServer;

public class RaonInterfaceServer extends NioServer {
	public RaonInterfaceServer() {
		try {
			this.getProcess_factory().setProcessClass(RaonSessionHandler.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
}
