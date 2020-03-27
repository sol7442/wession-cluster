package com.wowsanta.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wowsanta.server.Server;
import com.wowsanta.server.Service;
import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper=false)
public class NioServer extends JsonConfiguration implements Server, Runnable {
	int port;
	int core;
	
	transient ExecutorService server_excutor ;
	transient ExecutorService service_excutor;
	
	//https://www.programcreek.com/java-api-examples/?code=actiontech%2Fdble%2Fdble-master%2Fsrc%2Fmain%2Fjava%2Fcom%2Factiontech%2Fdble%2Fnet%2FNIOConnector.java#
	
	ServiceList<SocketChannel> service_list = new ServiceList<SocketChannel>();
	@Override
	public boolean initialize() {
		server_excutor  = Executors.newSingleThreadExecutor();
		return false;
	}

	@Override
	public void start() {
		server_excutor.execute(this);
	}

	@Override
	public void stop() {
		server_excutor.shutdown();
	}

	@Override
	public void run() {
		try {
			Selector selector = Selector.open();
			
	        ServerSocketChannel serverSocket = ServerSocketChannel.open();
	        serverSocket.configureBlocking(false);
	        
	        serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
	        serverSocket.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 16 * 2);
	        serverSocket.bind(new InetSocketAddress("localhost", port));
	        
	        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
	        log.info("server  : {}", serverSocket.getLocalAddress());

			while(true) {
				try {
					int selected = selector.select(1000L);
					log.debug("selected : {}",selected);
					
	                Set<SelectionKey> keys = selector.selectedKeys();
	                try {
	                    for (SelectionKey key : keys) {
	                        if (key.isValid() && key.isAcceptable()) {
	                            accept(serverSocket);
	                        } else {
	                            key.cancel();
	                        }
	                    }
	                } finally {
	                    keys.clear();
	                }
	                
				}catch (Exception e) {
					log.error(Thread.currentThread().getName(),e);
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			log.info("finish server : {}",this );
		}
	}

	private void accept(ServerSocketChannel server) {
        SocketChannel client = null;
        try {
        	client = server.accept();
        	client.configureBlocking(false);
        	// connetion..client / process
//        	
//        	FrontendConnection c = factory.make(channel);
//            c.setAccepted(true);
//            c.setId(ID_GENERATOR.getId());
//            NIOProcessor processor = DbleServer.getInstance().nextProcessor();
//            c.setProcessor(processor);
//
//            NIOReactor reactor = reactorPool.getNextReactor();
//            reactor.postRegister(c);

        } catch (Exception e) {
            closeClient(client);
        }
	}

	private void closeClient(SocketChannel client) {
		// TODO Auto-generated method stub
	}
}
