package test.raon;


import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.impl.server.RaonServerProcess;

public class ServerTest {
	
	static String fileName = "./config/nio.server.json"; 
			
	public static void main(String[] args) {
		ServerTest.create_save_server();
	}
	
	//@Test
	static public  void  create_save_server()  {
		NioServer server = new NioServer();
		server.setCore(2);
		server.setPort(5050);

		server.setProcessHandler(RaonServerProcess.class.getName());
		server.initialize();
		server.start();
		
		//server.save(fileName);
	}
}
