package test.raon.cfg;

import org.junit.Test;

import com.wowsanta.wession.cluster.ClusterProcessHandler;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.impl.WessionLancher;
import com.wowsanta.wession.impl.server.RaonInterfaceServer;
import com.wowsanta.wession.impl.server.RaonSessionHandler;
import com.wowsanta.wession.impl.session.RaonSession;
import com.wowsanta.wession.index.IndexRepository;

public class WessionCluserterConfigurationTest {
	
	final String file_name = "./config/wession_lancher.json";
	
	@Test
	public void create_test() {
		WessionLancher w = new WessionLancher();
		
		RaonInterfaceServer server = new RaonInterfaceServer();
		server.setIpAddr("127.0.0.1");
		server.setPort(5050);
		server.setCore(2);
		server.setProcessHandlerClass(RaonSessionHandler.class.getName());
		w.setInterfaceServer(server);
		
		ClusterRepository   cluster = new ClusterRepository();
		cluster.setIpAddr("127.0.0.1");
		cluster.setPort(5051);
		cluster.setCore(2);
		cluster.setProcessHandlerClass(ClusterProcessHandler.class.getName());
		w.setClusterServer(cluster);
		
		IndexRepository  index = new IndexRepository();
		index.getKeyList().add("name");
		index.getKeyList().add("userId");
		index.setWessionClassName(RaonSession.class.getName());

		w.setIndexService(index);
		w.save(file_name);
	}
	
	@Test
	public void load_test() {
		WessionLancher w = WessionLancher.load(file_name, WessionLancher.class);
		
		System.out.println(w.toString(true));
	}
}
