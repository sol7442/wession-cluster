package com.wowsanta.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.wowsanta.server.ProcessFactory;
import com.wowsanta.server.Server;
import com.wowsanta.server.Service;
import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class NioServer extends JsonConfiguration implements Server, Runnable {
	int port;
	int core;
	String processHandler;

	transient ProcessFactory process_factory = new ProcessFactory();
	transient ExecutorService server_excutor;
	transient ExecutorService service_excutor;
	// https://www.programcreek.com/java-api-examples/?code=actiontech%2Fdble%2Fdble-master%2Fsrc%2Fmain%2Fjava%2Fcom%2Factiontech%2Fdble%2Fnet%2FNIOConnector.java#

	@Override
	public boolean initialize() {
		server_excutor = Executors.newSingleThreadExecutor();
		service_excutor = Executors.newFixedThreadPool(10);
		try {
			process_factory.setProcessClass(this.processHandler);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void start() {
		server_excutor.execute(this);
	}

	@Override
	public void stop() {
		service_excutor.shutdown();
		server_excutor.shutdown();
	}

	@Override
	public void run() {
		try (Selector selector = Selector.open()) {
			try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {

				serverSocket.configureBlocking(false);
				serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				serverSocket.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 16 * 2);
				serverSocket.bind(new InetSocketAddress("localhost", port));

				log.info("server  : {}", serverSocket.getLocalAddress());

				serverSocket.register(selector, SelectionKey.OP_ACCEPT);
				while (selector.select() > 0) {
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						keys.remove();

						log.debug("ready opt : {} ", key.readyOps());

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
			}

		} catch (IOException e) {
			e.printStackTrace();
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
			int size = connection.read();
			if (size == -1) {
				log.info(" Connection closed by client : {}", connection.client.socket().getRemoteSocketAddress());
				connection.close();
			}

			NioProcess porcess = (NioProcess) process_factory.newProcess();
			porcess.connection = connection;

			final Future<NioConnection> future = service_excutor.submit(porcess);
			service_excutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						long start_time = System.nanoTime();
						NioConnection connection = future.get(1000, TimeUnit.SECONDS);
						long duratorion = System.nanoTime() - start_time;
						log.debug("porcess : {} - {} ", duratorion, connection);

						//connection.client.register(selector, SelectionKey.OP_WRITE, connection);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					}
//					catch (ClosedChannelException e) {
//						e.printStackTrace();
//					}
				}
			});

		} catch (IOException e) {
			connection.close();
			log.debug("receive : ", e.getMessage());
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

}
