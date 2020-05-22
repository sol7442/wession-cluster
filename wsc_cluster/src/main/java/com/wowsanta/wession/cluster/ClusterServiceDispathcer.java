package com.wowsanta.wession.cluster;

import java.io.IOException;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.Message;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.server.ServiceProcess;
import com.wowsanta.server.nio.NioConnection;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
public class ClusterServiceDispathcer extends ServiceDispatcher{

	long start_time ;
	long end_time;
	@Override
	public void before(ServiceProcess<?, ?> process) {
		try {
			start_time = System.currentTimeMillis();
		}catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
		}finally {
			LOG.application().debug("before : {} / {}", process.getClass().getName(), start_time);
		}
	}

	@Override
	public void dispatcher(ServiceProcess<?,?> process) {
		try {
			process.porcess();
		}catch (ServerException e) {
			LOG.application().error(e.getMessage(),e);
		}finally {
			try {
				ClusterResponse response = (ClusterResponse) process.getResponse();
				if(response != null) {
					response.getMessage().flush();
				}
			} catch (IOException e) {
				LOG.application().error(e.getMessage(), e);
			}
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
			LOG.application().error(e.getMessage(), e);
		}finally {
			LOG.application().debug("after : {} / {} ", process.getClass().getName(), end_time - start_time);
		}
	}
}
