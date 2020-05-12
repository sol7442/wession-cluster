package test.raon.wsc;

import java.util.Date;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.core.CoreRepository;
import com.wowsanta.wession.impl.session.RaonSession;
import com.wowsanta.wession.index.IndexRepository;
import com.wowsanta.wession.message.SearchMessage;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.repository.RespositoryException;

public class WessionClusterTest {

	String session_key = "5C71854B";
	
	static WessionCluster wession = null;// new WessionCluster();
	
	@Before
	public void before() {
		if(wession == null) {
			wession = new WessionCluster();
			CoreRepository core_repo = new CoreRepository();
			
			
			ClusterRepository cluster_repo = new ClusterRepository();
			ClusterNode node1 = new ClusterNode();
			node1.setName("dev_inst_1");
			cluster_repo.getNodes().add(node1);
			cluster_repo.initialize();
			

			IndexRepository index_repo = new IndexRepository();
			index_repo.initialize();
			
			wession.setCoreRepository(core_repo);
			wession.setClusterRepository(cluster_repo);
			wession.setIndexRepository(index_repo);
		}
	}
	
	@After
	public void after() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("-------------------------------------------------------");
	}
	
	@Test
	public void search_test() {
		System.out.println("[search_test]-----------------------------");
		RaonSession session1 = create(session_key + 1,"USER1","tester1");
		RaonSession session2 = create(session_key + 2,"USER1","tester2");
		RaonSession session3 = create(session_key + 3,"USER3","tester2");
		
		SearchMessage  request  = new SearchMessage();
		SearchResponse response = null;
		try {
			System.out.println("[search]-----------------------------");

			request.setFilter("name=USER1");
			request.setStartIndex(1);
			request.setCount(10);
			response = wession.search(request);
			
			System.out.println("[search]-----------------------------");

		} catch (RespositoryException e) {
			e.printStackTrace();
		}
		
		System.out.println("request  :  " + request);
		System.out.println("response :  " + response);
	}
	
	@Test
	public void delete_test() {
		System.out.println("[delete_test]-----------------------------");
		create(session_key,"name1","user1");
		RaonSession session = read(session_key);
		try {
			System.out.println("session :  " + session);
			System.out.println("[delete]-----------------------------");
			wession.delete(session);
			System.out.println("[delete]-----------------------------");

			session = read(session_key);
			System.out.println("session :  " + session);
		} catch (RespositoryException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void update_test() {
		System.out.println("[update_test]-----------------------------");
		create(session_key,"name1","user1");
		RaonSession session = read(session_key);
		session.setModifyTime(new Date());
		session.setAttribute("attr1","value1");
		try {
			wession.update(session);
			System.out.println("session :  " + session);
		} catch (RespositoryException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void read_test() {
		System.out.println("[read_test]-----------------------------");
		create(session_key,"name1","user1");
		RaonSession session = read(session_key);
		System.out.println("session :  " + session);
	}
	
	@Test
	public void create_test() {
		System.out.println("[create_test]-----------------------------");
		RaonSession session = create(session_key,"name1","user1");
		System.out.println("session :  " + session);
	}

	
	private RaonSession create(String key,String name, String id) {
		RaonSession session1 = new RaonSession();
		
		session1.setKey(key);
		session1.setName(name);
		session1.setUserId(id);
		
		session1.setCreateTime(new Date());
		session1.setModifyTime(new Date());
		
		try {
			wession.create(session1);
		} catch (RespositoryException e) {
			e.printStackTrace();
		}
		
		return session1;
	}
	private RaonSession read(String key) {
		RaonSession session = null;
		try {
			session = (RaonSession) wession.read(session_key);
		} catch (RespositoryException e) {
			e.printStackTrace();
		}
		return session;
	}
	
}
