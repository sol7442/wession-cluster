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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wowsanta.server.ProcessFactory;
import com.wowsanta.server.ProcessHandler;
import com.wowsanta.server.Response;
import com.wowsanta.server.Server;

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
	int threadSize     = 10;
	int processTimeout = 1000;
	
	String processHandlerClass;

	transient ProcessFactory process_factory = new ProcessFactory();
	transient ExecutorService server_excutor;
	transient ExecutorService service_excutor;

	transient Selector selector;
	transient ServerSocketChannel serverSocket;
	
	@Override
	public boolean initialize() {
		server_excutor  = Executors.newSingleThreadExecutor();
		service_excutor = Executors.newFixedThreadPool(threadSize);
		try {
			process_factory.setProcessClass(this.processHandlerClass);
		} catch (ClassNotFoundException e) {
			log.error("INITIALIZE FAILED : ",e);
		}
		return true;
	}

	@Override
	public void start() {
		log.info("SERVER START : {}", new Date());
		server_excutor.execute(this);
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
			
			int selected;
			while ( (selected = selector.select()) > 0) {
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

			NioProcessHandler process = (NioProcessHandler) process_factory.newProcess();
			
			process.bind(connection);
			log.debug("bind : {}", connection.client.socket().getRemoteSocketAddress());

			process.read();
			log.debug("read : {}", connection.client.socket().getRemoteSocketAddress());
			
			final Future<NioProcessHandler> future = service_excutor.submit(process);
			service_excutor.execute(new Runnable() {
				long start_time = System.currentTimeMillis();
				
				@Override
				public void run() {
					long duratorion = 0;
					int size = 0;
					NioProcessHandler handler;
					try {
						handler = future.get(processTimeout, TimeUnit.SECONDS);
						size = handler.write();
						duratorion = System.currentTimeMillis() - start_time;
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						log.debug("write : {} / {} ",size,duratorion);
					}
				}
			});

		} catch (IOException e) {
			connection.close();
			log.info(" Connection finished by client : {}", connection.client.socket().getRemoteSocketAddress());
		}
	}

	private void accept(Selector selector, SelectionKey key) {
		try {
			ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
			SocketChannel channel = serverChannel.accept();
			channel.configureBlocking(false);

			NioConnection connection = new NioConnection();
			connection.initialize();

			connection.setClient(channel);

			log.info("Connected to : {} ", channel.socket().getRemoteSocketAddress());

			channel.register(selector, SelectionKey.OP_READ, connection);

		} catch (IOException e) {
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
