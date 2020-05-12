package com.wowsanta.server.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceDispatcher;

import lombok.Data;

@Data
public abstract class NioConnection implements Connection{
	
//	protected String host;
//    protected int localPort;
//    protected int port;
//    protected SelectionKey key ;						
//    protected Selector selector;

    protected SocketChannel client;
    protected ByteBuffer readBuffer;
    protected ByteBuffer writeBuffer;
    
    public void initialize(int bufferSize) {
    	readBuffer  = ByteBuffer.allocate(bufferSize);
    	writeBuffer = ByteBuffer.allocate(bufferSize);
		
	}
    public int remaining() {
    	return readBuffer.remaining();
    }
    
    synchronized public int read0() throws ServerException{
    	return read();
    }
    synchronized public int write0() throws ServerException{
		return write();
	}
	
    public abstract int read() throws ServerException;
    public abstract int write() throws ServerException;
    
    @Override
    public void close() throws Exception{
    	client.close();
    }
}
