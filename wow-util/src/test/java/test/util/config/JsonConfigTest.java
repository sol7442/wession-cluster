package test.util.config;

import org.junit.Test;

public class JsonConfigTest {

	String file_name = "./data/config.jons";

	@Test
	public void create_test() {
		DaemonT t = new DaemonT();
		
		t.setServer(new ServerT());
		t.getServer().setServerName("tServer");
		
		t.getServer().getServices().add(new IndexService());
		t.getServer().getServices().add(new ClusterService());
		
		t.setInstanceName("tInstance");
		
		t.save(file_name);
	}
	
	@Test
	public void load_test() {
		DaemonT t = DaemonT.load(file_name, DaemonT.class);
		
		System.out.println(t);
	}
}
