package com.wowsanta.server.nio;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.wowsanta.server.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class NioService implements Service <ServerSocketChannel>, Runnable {
	String name;
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	transient Selector selector;
	transient ServerSocketChannel server;
	
	public NioService(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		log.debug("service start : {} " , this);
		
		try {
			selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			while(true) {
				int selected = selector.select();
				log.debug("selected : {}",selected);
				
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
	            Iterator<SelectionKey> iter = selectedKeys.iterator();
	            while (iter.hasNext()) {
	            	SelectionKey key = (SelectionKey) iter.next();
	            	if (!key.isValid()) { // 사용가능한 상태가 아니면 그냥 넘어감.
						continue;
					}
	            	if (key.isAcceptable()) { // select가 accept 모드이면
						accept(key);
					}
	            	
	            	
	            	
	            	
//	            	SelectionKey key = iter.next();
//	            	iter.remove();
//	            	
//	            	SocketChannel client = (SocketChannel) key.channel();
//	                client.read(buffer);
//	                
//	                log.debug(new String(buffer.array()));
//	                
//	                buffer.flip();
//	                client.write(buffer);
//	                buffer.clear();
	                
	            }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			log.info("finish server : {}",this );
		}
	}

	private void accept(SelectionKey key) {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel;

		try {
			channel = serverChannel.accept();
			channel.configureBlocking(false);
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connected to: " + remoteAddr);

			channel.register(key.selector(), SelectionKey.OP_READ); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void regist(ServerSocketChannel server) {
		this.server = server;
	}
}
