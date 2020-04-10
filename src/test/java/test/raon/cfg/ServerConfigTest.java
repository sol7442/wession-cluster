package test.raon.cfg;

import org.junit.Test;

import com.wowsanta.daemon.DaemonService;
import com.wowsanta.server.nio.NioServer;
import com.wowsanta.util.config.JsonConfiguration;
import com.wowsanta.wession.impl.server.RaonServerProcess;

public class ServerConfigTest {
	
	final String file_name = "./config/nio.server.json";
	
	@Test
	public void save_tet() {
		JsonConfiguration server = new NioServer();
		((NioServer)server).setCore(2);
		((NioServer)server).setPort(5050);
		((NioServer)server).setProcessHandler(RaonServerProcess.class.getName());
		
		server.save(file_name);
		
		
//		WowSantaDaemon daemon = new WowSantaDaemon();
//		daemon.setService(server);
//		daemon.save(file_name);
		
	}
	@Test
	public void load_tet() {
		try {
			@SuppressWarnings("unchecked")
			Class<JsonConfiguration>  config_class = (Class<JsonConfiguration>) Class.forName(NioServer.class.getName());
			DaemonService service = (DaemonService) JsonConfiguration.load(file_name, config_class);
			
			System.out.println(service);
			service.initialize();
			service.start();
			
			((NioServer)service).awaitTerminate();
			
			service.stop();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
