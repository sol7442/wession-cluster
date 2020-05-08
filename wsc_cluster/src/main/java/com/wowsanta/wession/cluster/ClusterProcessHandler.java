package com.wowsanta.wession.cluster;


import java.io.IOException;

import com.wowsanta.server.nio.NioProcessHandler;
import com.wowsanta.util.Buffer;
import com.wowsanta.wession.message.WessionRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClusterProcessHandler extends NioProcessHandler{

	@Override
	public int read() throws IOException {
		int length = -1;
		int remain = -1;
		
		WessionRequest request = null;
		try {
			remain = this.connection.remaining();
			if(remain > 0) {
				byte[] bytes = new byte[remain];
				
				length = this.connection.read(bytes);
				request = Buffer.toObject(bytes, WessionRequest.class);
			}
		}catch (Exception e) {
			log.error("read : {}/{}", length,remain);
			e.printStackTrace();
		}finally {
			log.debug("request : {}", request);
		}
		return length;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int write() throws IOException {
		return -1;
	}

	@Override
	public void error(Throwable e) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
