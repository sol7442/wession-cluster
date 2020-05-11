package test.raon.cfg;

import org.junit.Test;

import com.wowsanta.wession.cluster.ClusterServiceDispathcer;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.cluster.ClusterConnectionFactory;
import com.wowsanta.wession.cluster.ClusterRequestHandler;
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
		
		
		ClusterRepository   cluster = new ClusterRepository();
		cluster.setIpAddr("127.0.0.1");
		cluster.setPort(5051);
		cluster.setCore(2);
		
		//ServiceDispatcher service = new ClusterServiceDispathcer();
		//ClusterConnectionFactory factory = new ClusterConnectionFactory();
		
		cluster.setConnectionFactoryClass(ClusterConnectionFactory.class.getName());
		cluster.setServiceDispatcherClass(ClusterServiceDispathcer.class.getName());

		w.setClusterServer(cluster);
		
		IndexRepository  index = new IndexRepository();
		index.getKeyList().add("name");
		index.getKeyList().add("userId");
		index.setWessionClassName(RaonSession.class.getName());

		w.setInterfaceServer(server);
		w.setIndexService(index);
		
		w.save(file_name);
		
		System.out.println(w.toString(true));
	}
	
	//@Test
	public void load_test() {
		WessionLancher w = WessionLancher.load(file_name, WessionLancher.class);
		
		System.out.println(w.toString(true));
	}
}
