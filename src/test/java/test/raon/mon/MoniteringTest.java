package test.raon.mon;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.wession.cluster.ClusterClient;

public class MoniteringTest {
	@Test
	public void monitering_test() {
		
		List<Integer> port_list = new ArrayList<Integer>();
		port_list.add(5051);
		port_list.add(5052);
		port_list.add(5053);
		port_list.add(5054);
		port_list.add(5055);
		port_list.add(5056);
		
		while(true) {
			for (Integer port_num : port_list) {
				NioClient client = new ClusterClient("127.0.0.1",port_num);
			}
		}
	}
}
