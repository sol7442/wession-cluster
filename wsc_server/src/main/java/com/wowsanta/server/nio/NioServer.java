package com.wowsanta.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
	
	ServiceList service_list = new ServiceList();
	@Override
	public boolean initialize() {
		server_excutor  = Executors.newSingleThreadExecutor();
		service_excutor = Executors.newFixedThreadPool(core);
	
		for(int i=0; i<core;i++) {
			service_list.add(new NioService(":"+i ));
		}
		
		return false;
	}

	@Override
	public void start() {
		server_excutor.execute(this);
		for (Service service : service_list) {
			NioService nio_service = (NioService)service;
			log.debug("service start : {} " , nio_service);
			
			service_excutor.execute(nio_service);
		}
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
	        serverSocket.bind(new InetSocketAddress("localhost", port));
	        serverSocket.configureBlocking(false);
	        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
log.info("server run : {}", this);
			while(true) {
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
	            Iterator<SelectionKey> iter = selectedKeys.iterator();
	            while(iter.hasNext()) {
	            	SelectionKey key = iter.next();
	            	iter.remove();
	            	
	            	if(key.isAcceptable()) {
	            		log.debug("accept : {} ", key );
	            	}
	            }
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			log.info("finish server : {}",this );
		}
	}
}
