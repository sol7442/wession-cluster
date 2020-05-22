package test.wowsanta.server;


import com.wowsanta.server.ServerException;
import com.wowsanta.server.nio.NioServer;

public class DefalutServerTest {
	
	static String fileName = "./config/nio.server.json"; 
			
	public static void main(String[] args) {
		DefalutServerTest.create_save_server();
	}
	//@Test
	static public  void  create_save_server()  {
		NioServer server = new NioServer();
		server.setCore(2);
		server.setPort(5050);

		
		try {
			server.initialize();
			server.start();
		} catch (ServerException e) {
			e.printStackTrace();
		}
		
	}
}
