package com.wowsanta.server;


public class ProcessFactory {

	Class<ProcessHandler> processClass;
	
	
	@SuppressWarnings("unchecked")
	public void setProcessClass(String name) throws ClassNotFoundException {
		processClass = (Class<ProcessHandler>) Class.forName(name);
	}
	public void setProcessClass(Class<ProcessHandler> processClass) {
		this.processClass = processClass;
	}
	public ProcessHandler newProcess() {
		try {
			return processClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
