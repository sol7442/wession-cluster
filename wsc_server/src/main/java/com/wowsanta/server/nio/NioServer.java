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
import java.util.concurrent.TimeUnit;

import com.wowsanta.server.ConnectionFactory;
import com.wowsanta.server.Server;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.server.ServiceProcess;

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
	
	int processTimeout;
	int threadSize    ;
	int queueSize     ;
	int bufferSize    ;
	
	String connectionFactoryClass;
	String requestHandlerClass;
	String serviceDispatcherClass;
	
	
	transient ConnectionFactory connectionFactory;
	transient ServiceDispatcher serviceDispatcher;
	

	transient ExecutorService serverExecutor;
	transient ExecutorService serviceExecutor;
	
	transient Selector selector;
	transient ServerSocketChannel serverSocket;
	transient BlockingQueue<ServiceProcess<?,?>> rquestQueue ;
	
	@Override
	public boolean initialize() {
		try {
			serverExecutor  = Executors.newSingleThreadExecutor();
			serviceExecutor = Executors.newFixedThreadPool(threadSize);
			rquestQueue = new ArrayBlockingQueue<ServiceProcess<?,?>>(queueSize);

			
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
			this.connectionFactory.setBufferSize(bufferSize);
			
			serverExecutor.execute(this);
			for(int i=0; i<threadSize; i++) {
				ServiceDispatcher serviceDispatcher = (ServiceDispatcher) Class.forName(serviceDispatcherClass).newInstance();
				serviceDispatcher.bindRquestQueue(this.rquestQueue);
				
				serviceExecutor.execute(serviceDispatcher);
			}
			
		} catch (Exception e) {
			log.error("Server Handler Class Error : " + e.getMessage(), e);
		} 
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
		
		serverExecutor.shutdown();
		try {
			isTeminated = serverExecutor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("shutdown error : {} ",isTeminated, e);
		}
		
		serviceExecutor.shutdown();
		try {
			isTeminated = serviceExecutor.awaitTermination(3, TimeUnit.SECONDS);
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
		NioConnection connection = (NioConnection) key.attachment();
		try {
			int read = connection.read0();
			log.debug("key({}) receive : {}", key.hashCode(), read);
			if (read == -1) {
				connection.close();
				log.info(" Connection closed by client : {}", connection.client.socket().getRemoteSocketAddress());
			}
		}catch (Exception e) {
			receive_error(connection, e);
		}
	}

	private void receive_error(NioConnection connection, Exception error) {
		log.error("Connection Receive ERROR : {} ", error.getMessage() ,error);
		try {
			connection.close();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	private void accept(Selector selector, SelectionKey key) {
		try {
			ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = serverChannel.accept();
			channel.configureBlocking(false);

			NioConnection connection = (NioConnection) connectionFactory.build(channel);
			log.debug("key({}) accept : {}", key.hashCode(), connection.getClient());
			
			channel.register(selector, SelectionKey.OP_READ, connection);

		} catch (IOException e) {
			log.error("accept : ", e);
		} catch (ServerException e) {
			log.error("accept : ", e);
		} 
	}

	public void awaitTerminate() {
		try {
			this.serverExecutor.awaitTermination(10,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
