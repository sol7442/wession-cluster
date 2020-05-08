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
import com.wowsanta.server.ProcessHandler;
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
	}

	@Override
	public void register(NioConnection t) {
		// TODO Auto-generated method stub
		
	}


}
