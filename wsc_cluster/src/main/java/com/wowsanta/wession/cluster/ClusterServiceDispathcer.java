package com.wowsanta.wession.cluster;

import com.wowsanta.server.Message;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.server.ServiceProcess;
import com.wowsanta.server.nio.NioConnection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper=false)
public class ClusterServiceDispathcer extends ServiceDispatcher{

	long start_time ;
	long end_time;
	@Override
	public void before(ServiceProcess<?, ?> process) {
		try {
			start_time = System.currentTimeMillis();
			log.debug("porcess run : {} ", process);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
	}

	@Override
	public void dispatcher(ServiceProcess<?,?> process) {
		try {
			process.porcess();
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void after(ServiceProcess<?,?> process) {
		try {
			
			ClusterResponse response = (ClusterResponse) process.getResponse();
			if(response != null) {
				Message message = response.getMessage();
				
				NioConnection connection = (NioConnection) process.getConnection();
				
				connection.write(message.toBytes());
				connection.write0();
			}
			end_time = System.currentTimeMillis();
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			log.debug("duration : {} ", end_time - start_time);
		}
	}
}
