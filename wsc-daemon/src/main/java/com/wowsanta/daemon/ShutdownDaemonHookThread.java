package com.wowsanta.daemon;



import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper=false)
public class ShutdownDaemonHookThread extends Thread {
	
	WowSantaDaemon daemon;
	public void run() {
		log.info("SYSTEM SHUTDOWN RUN...");
		if(this.daemon != null) {
			this.daemon.destroy();
		}		
		log.info("SYSTEM SHUTDOWN DISTROY...");
	}
	public void attachShutDownHook(WowSantaDaemon daemon) {
		log.info("SET SYSTEM SHUTDONW HOOK : ");
		Runtime.getRuntime().addShutdownHook(this);
		this.daemon = daemon;
		
	}
}
