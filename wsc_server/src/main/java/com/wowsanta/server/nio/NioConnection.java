package com.wowsanta.server.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ProcessHandler;
import com.wowsanta.util.Hex;

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
    
    protected ProcessHandler porcess;

    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;
    
    public void initialize() {
    	readBuffer  = ByteBuffer.allocate(1024);
    	writeBuffer = ByteBuffer.allocate(1024);
    }
    
    public int remaining() {
    	return readBuffer.remaining();
    }
	synchronized public int read0() throws IOException {
		readBuffer.clear();
		int size = client.read(readBuffer);
		readBuffer.flip();
		
		log.debug("read0 : {}", size);
		return size;
	}
	@Override
	synchronized public void write0() throws IOException {
		writeBuffer.flip();
		int size = client.write(writeBuffer);
		writeBuffer.clear();
		
		log.debug("write0 : {}", size);
	}
	@Override
	synchronized public int read(byte[] data) throws IOException {
		readBuffer.get(data);
		log.debug("read1 : {}", data.length);
		return data.length;
	}
	synchronized public void write(byte[] data) throws IOException{
		writeBuffer.put(data);
		log.debug("write1 : {}", Hex.toHexString(data));
	}
	
	
	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
