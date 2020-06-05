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

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ConnectionFactory;
import com.wowsanta.server.Server;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.server.ServiceProcess;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NioServer extends Server implements  Runnable {
	String name;
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
	public boolean initialize() throws ServerException {
		try {
			LOG.system().debug("NioServer : {} \n{}",this.name, toString(true));
			
			serverExecutor  = Executors.newSingleThreadExecutor();
			serviceExecutor = Executors.newFixedThreadPool(threadSize);
			rquestQueue = new ArrayBlockingQueue<ServiceProcess<?,?>>(queueSize);

			
			
			this.serviceDispatcher = (ServiceDispatcher) Class.forName(serviceDispatcherClass).newInstance();
			this.serviceDispatcher.bindRquestQueue(this.rquestQueue);
			
			this.connectionFactory = (ConnectionFactory) Class.forName(connectionFactoryClass).newInstance();
			this.connectionFactory.bindRquestQueue(this.rquestQueue);
			this.connectionFactory.setBufferSize(bufferSize);
			
			return true;
		} catch (Exception e) {			
			LOG.system().error("Server Handler Class Error : " + e.getMessage(), e);
			throw new ServerException(e.getMessage(), e);
		} 
	}

	@Override
	public void start() throws ServerException {
		try {
			for(int i=0; i<threadSize; i++) {
				ServiceDispatcher serviceDispatcher = (ServiceDispatcher) Class.forName(serviceDispatcherClass).newInstance();
				serviceDispatcher.bindRquestQueue(this.rquestQueue);
				serviceExecutor.execute(serviceDispatcher);
			}

			selector = Selector.open();
			serverSocket = ServerSocketChannel.open();		
			serverSocket.configureBlocking(false);
			serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			serverSocket.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 16 * 2);
			serverSocket.bind(new InetSocketAddress(ipAddr, port));

			LOG.system().info("{}/{}/{} - {}", this.getName(), serviceDispatcherClass, threadSize, new Date());
			serverExecutor.execute(this);
		} catch (Exception e) {
			LOG.application().error("Server Handler Class Error : " + e.getMessage(), e);
			throw new ServerException(e.getMessage(), e);
		} 
	}

	@Override
	public void stop() {
		boolean isTeminated = false;
		
		try {
			serverSocket.close();
			selector.close();
			LOG.application().debug("close server");
		} catch (IOException e) {
			LOG.application().error("SERVER CLOSE : ", e);
		}
		
		serverExecutor.shutdown();
		try {
			isTeminated = serverExecutor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOG.application().error("shutdown error : {} ",isTeminated, e);
		}
		
		serviceExecutor.shutdown();
		try {
			isTeminated = serviceExecutor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOG.application().error("shutdown error : {} ",isTeminated, e);
		}
		
		LOG.application().info("SERVER STOP {} : {}", isTeminated, new Date());
	}

	@Override
	public void run() {
		try {
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
			LOG.system().error("SERVER ERROR : {}",e.getMessage(),e);
		} finally {
			LOG.system().info("finish server : {}/{}", this.getName(), new Date());
		}
	}

	private void send(Selector selector, SelectionKey key) {
		try {
			NioConnection connection = (NioConnection) key.attachment();
			connection.client.register(selector, SelectionKey.OP_READ, connection);
			LOG.application().debug("send : {} ", connection);
		} catch (IOException e) {
			LOG.application().error("accept : ", e);
		}
	}

	private void receive(final Selector selector, SelectionKey key) {
		NioConnection connection = (NioConnection) key.attachment();
		try {
			int read = connection.read0();
			LOG.application().debug("key({}) receive : {} / {}", key.hashCode(),connection.getClient().getRemoteAddress(), read);
			if (read == -1) {
				connection.close();
				LOG.application().info(" Connection closed by client : {}", connection.client.socket().getRemoteSocketAddress());
			}
		}catch (Exception e) {
			receive_error(connection, e);
		}
	}

	private void receive_error(NioConnection connection, Exception error) {
		try {
			connection.close();
		} catch (Exception e) {
			LOG.application().error(e.getMessage(),e);
		}finally {
			LOG.application().error("Connection Receive ERROR : {} ", error.getMessage());
		}
	}

	private void accept(Selector selector, SelectionKey key) {
		try {
			ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = serverChannel.accept();
			channel.configureBlocking(false);

			NioConnection connection = (NioConnection) connectionFactory.build(channel);
			LOG.application().debug("key({}) accept : {}", key.hashCode(), connection.getClient());
			
			channel.register(selector, SelectionKey.OP_READ, connection);

		} catch (IOException e) {
			LOG.application().error("accept : ", e);
		} catch (ServerException e) {
			LOG.application().error("accept : ", e);
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
