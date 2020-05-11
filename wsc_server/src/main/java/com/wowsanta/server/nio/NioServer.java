package com.wowsanta.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ConnectionFactory;
import com.wowsanta.server.RequestHandler;
import com.wowsanta.server.HandlerFactory;
import com.wowsanta.server.Request;
import com.wowsanta.server.Server;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceDispatcher;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class NioServer extends Server implements  Runnable {
	String ipAddr;
	int port;
	int core;
	int threadSize     = 5;
	int processTimeout = 1000;
	int queueSize      = 100;
		
//	String processHandlerClass;
//	String connectionHandlerClass;
//	transient HandlerFactory handler_factory = new HandlerFactory();
	transient ExecutorService server_excutor;
	transient ExecutorService service_excutor;
	
	//transient ConnectionHandler connectionHandler;
	
	String connectionFactoryClass;
	String requestHandlerClass;
	String serviceDispatcherClass;
	
	
	transient ConnectionFactory connectionFactory;
	transient RequestHandler 	requestHandler;
	transient ServiceDispatcher serviceDispatcher;
	
	
	transient Selector selector;
	transient ServerSocketChannel serverSocket;
	transient BlockingQueue<Request> rquestQueue ;
	
	@Override
	public boolean initialize() {
		server_excutor  = Executors.newSingleThreadExecutor();
		service_excutor = Executors.newFixedThreadPool(threadSize);
		rquestQueue = new ArrayBlockingQueue<Request>(queueSize);
		
		try {
			
			this.connectionFactory = (ConnectionFactory) Class.forName(connectionFactoryClass).newInstance();
			this.serviceDispatcher = (ServiceDispatcher) Class.forName(serviceDispatcherClass).newInstance();
			
			this.connectionFactory.bindRquestQueue(this.rquestQueue);
			this.serviceDispatcher.bindRquestQueue(this.rquestQueue);
			
			return true;
		} catch (Exception e) {
			log.error("Server Handler Class Error : " + e.getMessage(), e);
		} 
		
		return false;
	}

	@Override
	public void start() {
		log.info("SERVER START : {}", new Date());
		try {
			
			this.connectionFactory = (ConnectionFactory) Class.forName(connectionFactoryClass).newInstance();
			this.connectionFactory.bindRquestQueue(this.rquestQueue);
			server_excutor.execute(this);
			
			for(int i=0; i<threadSize; i++) {
				ServiceDispatcher serviceDispatcher = (ServiceDispatcher) Class.forName(serviceDispatcherClass).newInstance();
				serviceDispatcher.bindRquestQueue(this.rquestQueue);
				
				service_excutor.execute(serviceDispatcher);
			}
			
		} catch (Exception e) {
			log.error("Server Handler Class Error : " + e.getMessage(), e);
		} 
//
//
//		if(this.serviceDispatcher != null) {
//			server_excutor.execute(this);
//			service_excutor.execute(serviceDispatcher);	
//		}
	}

	@Override
	public void stop() {
		boolean isTeminated = false;
		
		try {
			serverSocket.close();
			selector.close();
			log.debug("close server");
		} catch (IOException e) {
			log.error("SERVER CLOSE : ", e);
		}
		
		server_excutor.shutdown();
		try {
			isTeminated = server_excutor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("shutdown error : {} ",isTeminated, e);
		}
		
		service_excutor.shutdown();
		try {
			isTeminated = service_excutor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("shutdown error : {} ",isTeminated, e);
		}
		
		log.info("SERVER STOP {} : {}", isTeminated, new Date());
	}

	@Override
	public void run() {
		try {
			selector = Selector.open();
			serverSocket = ServerSocketChannel.open();		

			serverSocket.configureBlocking(false);
			serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			serverSocket.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 16 * 2);
			serverSocket.bind(new InetSocketAddress(ipAddr, port));

			log.info("server  : {}", serverSocket);
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);
			log.info("server register : {}",new Date());
			
			while ( selector.select() > 0) {
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();

					if (!key.isValid())
						continue;

					if (key.isAcceptable()) {
						accept(selector, key);
					}else
					if (key.isReadable()) {
						receive(selector, key);
					}else
					if (key.isWritable()) {
						send(selector, key);
					}
				}
			}
		} catch (IOException e) {
			log.error("SERVER ERROR : {}",e.getMessage(),e);
		} finally {
			log.info("finish server : {}", this);
		}
	}

	private void send(Selector selector, SelectionKey key) {
		try {
			NioConnection connection = (NioConnection) key.attachment();
			connection.client.register(selector, SelectionKey.OP_READ, connection);
			log.debug("send : {} ", connection);
		} catch (IOException e) {
			log.error("accept : ", e);
		}
	}

	private void receive(final Selector selector, SelectionKey key) {
		NioConnection connection = null;
		try {
			connection = (NioConnection) key.attachment();
			int size = connection.read0();
			if (size == -1) {
				log.info(" Connection closed by client : {}", connection.client.socket().getRemoteSocketAddress());
				connection.close();
				return;
			}
		} catch (Exception e) {
			connection.close();
			log.info(" Connection finished by client : {}", connection.client.socket().getRemoteSocketAddress());
		}
	}

	private void accept(Selector selector, SelectionKey key) {
		try {
			ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = serverChannel.accept();
			channel.configureBlocking(false);

			Connection connection = connectionFactory.build(channel);
			log.info("Connected to : {} ", channel.socket().getRemoteSocketAddress());

			channel.register(selector, SelectionKey.OP_READ, connection);

		} catch (IOException e) {
			log.error("accept : ", e);
		} catch (ServerException e) {
			log.error("accept : ", e);
		} 
	}

	public void awaitTerminate() {
		try {
			this.server_excutor.awaitTermination(10,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
