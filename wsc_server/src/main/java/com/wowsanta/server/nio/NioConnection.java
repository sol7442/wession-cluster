package com.wowsanta.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.wowsanta.server.Connection;
import com.wowsanta.server.RequestHandler;
import com.wowsanta.server.ProcessHandler;
import com.wowsanta.server.Request;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.util.Hex;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class NioConnection implements Connection{
//	private transient final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//  private transient final Lock readLock = readWriteLock.readLock();
//	private transient final Lock writeLock = readWriteLock.writeLock();
	
	
	protected String host;
    protected int localPort;
    protected int port;

    protected SelectionKey key ;						
    protected SocketChannel client;
    protected Selector selector;
    
   // protected ProcessHandler porcess;
	
    protected ByteBuffer readBuffer;
    protected ByteBuffer writeBuffer;
    
    private RequestHandler requestHandler;
    private ServiceDispatcher dispatcher;
    
    public void initialize() {
    	readBuffer  = ByteBuffer.allocate(1024);
    	writeBuffer = ByteBuffer.allocate(1024);
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
    
	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
