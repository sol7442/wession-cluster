package test.raon.cfg;

import org.junit.Test;

import com.wowsanta.wession.impl.WessionLancher;
import com.wowsanta.wession.impl.server.RaonInterfaceServer;
import com.wowsanta.wession.impl.server.RaonSessionHandler;

public class ServerConfigTest {
	
	final String file_name = "./config/wession.json";
	
	@Test
	public void create_test() {
		WessionLancher w = new WessionLancher();
		RaonInterfaceServer server = new RaonInterfaceServer();
		server.setIpAddr("127.0.0.1");
		server.setPort(5050);
		server.setCore(2);
		server.setProcessHandlerClass(RaonSessionHandler.class.getName());
		
		//w.setServer(server);
		w.save(file_name);
	}
	
	@Test
	public void load_test() {
		WessionLancher w = WessionLancher.load(file_name, WessionLancher.class);
		
		System.out.println(w);
	}
}
