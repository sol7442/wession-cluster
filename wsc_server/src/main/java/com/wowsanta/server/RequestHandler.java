package com.wowsanta.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

public interface RequestHandler extends Handler{
	Request parse(ByteBuffer buffer) throws IOException,ServerException;
	void bindRquestQueue(BlockingQueue<Request> requestQueue);
}
