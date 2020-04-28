package test.raon.cfg;

import org.junit.Test;

import com.wowsanta.wession.impl.Wession;
import com.wowsanta.wession.impl.server.RaonInterfaceServer;

public class ServerConfigTest {
	
	final String file_name = "./config/wession.json";
	
	@Test
	public void create_test() {
		Wession w = new Wession();
		RaonInterfaceServer server = new RaonInterfaceServer();
		server.setIpAddr("127.0.0.1");
		server.setPort(5050);
		server.setCore(2);
		
		w.setServer(server);
		w.save(file_name);
	}
	
	@Test
	public void load_test() {
		Wession w = Wession.load(file_name, Wession.class);
		
		System.out.println(w);
	}
}
