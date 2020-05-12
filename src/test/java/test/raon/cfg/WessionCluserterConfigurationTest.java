package test.raon.cfg;

import org.junit.Test;

import com.wowsanta.wession.cluster.ClusterServiceDispathcer;
import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.cluster.ClusterConnectionFactory;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.impl.WessionLancher;
import com.wowsanta.wession.impl.server.RaonInterfaceServer;
import com.wowsanta.wession.impl.session.RaonSession;
import com.wowsanta.wession.index.IndexRepository;
import com.wowsanta.wession.manager.ClusterManager;

public class WessionCluserterConfigurationTest {
	
	final String file_name = "./config/wession_lancher.json";
	
	@Test
	public void create_test() {
		WessionLancher w = new WessionLancher();
		
		RaonInterfaceServer server = new RaonInterfaceServer();
		server.setIpAddr("127.0.0.1");
		server.setPort(5050);
		server.setCore(2);
		
		ClusterManager cluseter_manager = ClusterManager.getInstance();
		NioServer cluser_server = new NioServer();
		cluser_server.setIpAddr("127.0.0.1");
		cluser_server.setPort(5051);
		cluser_server.setCore(2);
		
		cluser_server.setConnectionFactoryClass(ClusterConnectionFactory.class.getName());
		cluser_server.setServiceDispatcherClass(ClusterServiceDispathcer.class.getName());
		cluser_server.setThreadSize(10);
		cluser_server.setQueueSize(10);
		
		ClusterNode node1 = new ClusterNode();
		node1.setName("dev_02");
		node1.setAddress("127.0.0.1");
		node1.setPort(5051);
		node1.setMaxIdle(5);
		node1.setMinIdle(2);
		node1.setTestWhileIdle(false);
		
		cluseter_manager.addNode(node1);
		
		
		cluseter_manager.setClusterServer(cluser_server);
		
		w.setClusterManger(cluseter_manager);
		
		IndexRepository  index = new IndexRepository();
		index.getKeyList().add("name");
		index.getKeyList().add("userId");
		index.setWessionClassName(RaonSession.class.getName());

		//w.setInterfaceServer(server);
		w.setIndexService(index);
		
		w.save(file_name);
		
		System.out.println(w.toString(true));
	}
	
	@Test
	public void load_test() {
		WessionLancher w = WessionLancher.load(file_name, WessionLancher.class);
		
		System.out.println(w.toString(true));
	}
}
