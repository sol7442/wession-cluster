package com.wowsanta.server.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceProcess;

import lombok.Data;

@Data
public abstract class NioConnection implements Connection{

    protected SocketChannel client;
    protected ByteBuffer readBuffer;
    protected ByteBuffer writeBuffer;
    protected BlockingQueue<ServiceProcess<?,?>> rquestQueue ;
    
    public void initialize(int bufferSize) {
    	readBuffer  = ByteBuffer.allocate(bufferSize);
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
    public abstract void write(byte[] data) throws ServerException;
    @Override
    public void close() throws Exception{
    	client.close();
    }
}
