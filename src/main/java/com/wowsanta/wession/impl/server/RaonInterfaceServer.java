package com.wowsanta.wession.impl.server;

import com.wowsanta.server.nio.NioServer;

public class RaonInterfaceServer extends NioServer {
	public RaonInterfaceServer() {
		setProcessHandlerClass(RaonServerProcess.class.getName());
	}
}
