package com.wowsanta.wession.cluster;


import java.util.Date;

import com.wowsanta.server.Request;
import com.wowsanta.server.ServiceDispatcher;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class ClusterServiceDispathcer extends ServiceDispatcher{
	
	@Override
	public void dispatcher(Request request) {
		try {
			log.debug("request 0 : {} ", request);
			Thread.sleep(500);
			log.debug("request 1 : {} ", request);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			log.debug("some ... : {}",new Date());
		}
	}
}
