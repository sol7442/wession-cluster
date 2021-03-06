package test.raon;



import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.cluster.ClusterClient;
import com.wowsanta.wession.message.CreateMessage;

public class ClientTest {

	String ip = "127.0.0.1";
	int port = 5051;
	@Test
	public void write_test() {
		NioClient client = new ClusterClient(ip, port);
		try 
		{
			client.connect();
			
			RaonSession session = new RaonSession();
			session.setKey(Hex.toHexString(new Random().nextInt()));
			session.setUserId("tester1");
			session.setCreateTime(new Date());
			session.setModifyTime(new Date());
			session.setAttribute("key1","value1");
			session.setAttribute("key2","value2");
			
			CreateMessage request = new CreateMessage(session);
			request.setWession(session);
			client.write(request);
			
			client.write(request);
			client.write(request);
			client.write(request);
			client.write(request);
			client.write(request);
			client.write(request);
			
			Thread.sleep(1000);
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
