package com.wowsanta.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.wowsanta.server.Connection;
import com.wowsanta.server.Process;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class NioConnection implements Connection{
    protected String host;
    protected int localPort;
    protected int port;

    protected SelectionKey key ;						
    protected SocketChannel client;
    protected Selector selector;
    
    protected Process porcess;

    public ByteBuffer readBuffer;
//    public ByteBuffer writeBuffer;
    
    public void initialize() {
    	readBuffer  = ByteBuffer.allocate(1024);		
    }
	public int read() throws IOException {
		readBuffer.clear();
		
		return client.read(readBuffer);
	}
	
	public void write(byte[] data) throws IOException{
		client.write(ByteBuffer.wrap(data));
	}
	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
//	public void enableRead(Selector selector) throws ClosedChannelException {
//		this.selector = selector;
//		this.key = client.register(selector, SelectionKey.OP_READ);
//		this.key.attach(this);
//		
//	}
//	public void enableWrite() throws ClosedChannelException {
//		this.key = client.register(this.selector, SelectionKey.OP_WRITE);
//		this.key.attach(this);
//		this.selector.wakeup();
//	}

}
