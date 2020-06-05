package test.raon.cfg;

import org.junit.Test;

import com.wowsanta.wession.cluster.ClusterServiceDispathcer;
import com.wowsanta.raon.impl.policy.RaonSessionPolicy;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.cluster.ClusterConnectionFactory;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.impl.server.RaonSessionServerConnectionFactory;
import com.wowsanta.wession.impl.WessionLancher;
import com.wowsanta.wession.impl.server.RaonSessionServer;
import com.wowsanta.wession.impl.server.RaonSessionServiceDispatcher;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.manager.CoreManager;
import com.wowsanta.wession.manager.IndexManager;
import com.wowsanta.wession.manager.PolicyManager;

public class WessionCluserterConfigurationTest {
	
	final String file_name = "./config/wession_lancher.json";
	
	@Test
	public void load_test() {
		WessionLancher w = WessionLancher.load(file_name);
		System.out.println(w.toString(true));

		String policy_file = "./config/raon_policy.json";
		RaonSessionPolicy policy = new RaonSessionPolicy();
		policy.save(policy_file);
		
		PolicyManager.getInstance().setPolicyClass(RaonSessionPolicy.class.getName());
		PolicyManager.getInstance().setPolicyFile(policy_file);;
		
		w.setPolicyManager(PolicyManager.getInstance());
		
		w.save(file_name);
		System.out.println(w.toString(true));
	}
	
	
	
	//@Test
	public void create_test() {
		WessionLancher w = new WessionLancher();
		RaonSessionServer session_server = new RaonSessionServer();
		session_server.setIpAddr("127.0.0.1");
		session_server.setPort(5050);
		session_server.setCore(2);
		session_server.setThreadSize(10);
		session_server.setQueueSize(10);
		session_server.setBufferSize(1024);
		
		session_server.setConnectionFactoryClass(RaonSessionServerConnectionFactory.class.getName());
		session_server.setServiceDispatcherClass(RaonSessionServiceDispatcher.class.getName());
		
		ClusterManager cluseter_manager = ClusterManager.getInstance();
		NioServer cluser_server = new NioServer();
		cluser_server.setIpAddr("127.0.0.1");
		cluser_server.setPort(6060);
		cluser_server.setCore(2);
		cluser_server.setThreadSize(10);
		cluser_server.setQueueSize(10);
		cluser_server.setBufferSize(1024);
		
		cluser_server.setConnectionFactoryClass(ClusterConnectionFactory.class.getName());
		cluser_server.setServiceDispatcherClass(ClusterServiceDispathcer.class.getName());

		
		ClusterNode node1 = new ClusterNode();
		node1.setName("dev_02");
		node1.setAddress("127.0.0.1");
		node1.setPort(6060);
//		node1.setMaxIdle(5);
//		node1.setMinIdle(2);
//		node1.setTestWhileIdle(false);
		
		cluseter_manager.setClusterNode(node1);
		
		
		cluseter_manager.setClusterServer(cluser_server);
		
		w.setClusterManger(cluseter_manager);
		
		IndexManager  index = IndexManager.getInstance();
		index.getKeyList().add("name");
		index.getKeyList().add("userId");
		index.setWessionClassName(RaonSession.class.getName());

		w.setInterfaceServer(session_server);
		w.setIndexManager(index);
		
		w.save(file_name);
		
		System.out.println(w.toString(true));
	}
	
	
}
