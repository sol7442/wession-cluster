package test.raon.cluster;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.repository.RespositoryException;

public class ClusterNodeTest {

	String ip = "127.0.0.1";
	int port = 5051;
	
	@Test
	public void node_create_test() {
		ClusterNode node = new ClusterNode();
		node.setAddress(ip);
		node.setPort(port);
		
		
		node.initialize();
		try {
			int count = 10;
			ExecutorService fixed_thread = Executors.newFixedThreadPool(3);
			while(count > 0) {
				fixed_thread.execute(new Runnable() {
					@Override
					public void run() {
						try {
							node.create(create_random_raon_session());
						} catch (RespositoryException e) {
							e.printStackTrace();
						}
					}
				});
				count--;
			}
			
			Thread.sleep(1000);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
	private RaonSession create_random_raon_session() {
		RaonSession session = new RaonSession();
		session.setKey(Hex.toHexString(new Random().nextInt()));
		session.setUserId("tester1");
		session.setCreateTime(new Date());
		session.setModifyTime(new Date());
		session.setAttribute("key1","value1");
		session.setAttribute("key2","value2");
		
		return session;
	}
}
