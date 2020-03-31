package com.wowsanta.server;

import com.wowsanta.server.nio.NioProcess;

public class ProcessFactory {

	Class<NioProcess> processClass;
	
	
	@SuppressWarnings("unchecked")
	public void setProcessClass(String name) throws ClassNotFoundException {
		processClass = (Class<NioProcess>) Class.forName(name);
	}
	public NioProcess newProcess() {
		try {
			return processClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
