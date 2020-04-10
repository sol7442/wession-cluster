package com.wowsanta.daemon;

public interface DaemonService {
	public boolean initialize();
	public void start();
	public void stop();
}
