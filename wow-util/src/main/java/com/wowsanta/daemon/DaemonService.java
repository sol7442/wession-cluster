package com.wowsanta.daemon;

public interface DaemonService {
	public boolean initialize(String config);
	public void start();
	public void stop();
}
