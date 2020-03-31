package com.wowsanta.server.nio;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wowsanta.server.Connection;
import com.wowsanta.server.Process;
import com.wowsanta.server.ProcessFactory;
import com.wowsanta.server.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class NioService implements Service <NioConnection>, Runnable{
	String name;
	ByteBuffer buffer = ByteBuffer.allocate(1024);
//	transient Selector selector;
	transient ServerSocketChannel server;
	
	transient ExecutorService prosess_excutor;
	transient ProcessFactory  process_factory;
	
	public NioService(String name) {
		this.name = name;
		prosess_excutor = Executors.newFixedThreadPool(10);
	}
	
//	public void register(NioConnection connection) {
//		try {
//			selector.wakeup();
//			connection.enableRead(selector);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}	
		
	
	
	@Override
	public void run() {
//		try {
//			selector = Selector.open();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		log.debug("service start : {} " , this.server);
		
		try (
				Selector selector = Selector.open();
				
			){
			while(true) {
				int selected = selector.select();
				Set<SelectionKey> keys = null;
				try {
					keys = selector.selectedKeys();
					
		            Iterator<SelectionKey> iter = keys.iterator();
		            while (iter.hasNext()) {
		            	SelectionKey key = (SelectionKey) iter.next();
		            	iter.remove();
		            	if(!key.isValid()) continue;
		            	
		            	if(key.isAcceptable()) {
		            		accept();
		            	}
		            	if(key.isReadable()) {
		            		read()
		            	}
		            	if(key.isWritable()) {
		            		write();
		            	}
//		            	
//		            	if (key.isValid() && key.isReadable()) {
//		            		NioConnection connection =  (NioConnection) key.attachment();
//		            		connection.read();
//		            		
//		            		NioProcess proces = (NioProcess) process_factory.newProcess();
//		            		proces.setConnection(connection);
//		            		
//		            		prosess_excutor.execute(proces);
//						}
////		            	
//		            	if (key.isValid() && key.isWritable()) { 
//		            		NioConnection connection = (NioConnection) key.attachment();
//		            		connection.write();
//		            		connection.enableRead(this.selector);
//						}
		            }
				}finally {
				}
				log.debug("selected >> 2 : {}",selected);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			log.info("finish server : {}",this );
		}
	}

	@Override
	public void register(NioConnection t) {
		// TODO Auto-generated method stub
		
	}


}
