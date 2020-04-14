package com.wowsanta.daemon;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.apache.commons.daemon.DaemonInitException;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class WowSantaDaemon implements Daemon {
	private static WowSantaDaemon daemon = new WowSantaDaemon();
	private static ShutdownDaemonHookThread shutdownHook;
	
	public static void main(String[] args) {
		try {
			shutdownHook = new ShutdownDaemonHookThread();
			shutdownHook.attachShutDownHook(daemon);
			
			daemon.init(new DaemonContext() {
				@Override
				public DaemonController getController() {return null;}
				@Override
				public String[] getArguments() {return args;}
			});
			
			daemon.start();
		} catch (Exception e) {
			log.error("DAEMON START ERROR : ",e);
		}
	}
	
	DaemonService service;
	@Override
	public void init(DaemonContext context) throws DaemonInitException, Exception {
		String[] args = context.getArguments();
		
		log.debug("DAEMON SERVICE : {} ",args[0]);
		log.debug("DAEMON CONFIG  : {} ",args[1]);
		
		@SuppressWarnings("unchecked")
		Class<DaemonService> config_class = (Class<DaemonService>) Class.forName(args[0]);
		service = config_class.newInstance();
		service.initialize(args[1]);
	}

	@Override
	public void start() throws Exception {
		service.start();
	}

	@Override
	public void stop() throws Exception {
		service.stop();
	}

	@Override
	public void destroy() {
		service.stop();
	}
}
