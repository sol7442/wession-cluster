package com.wowsanta.wession.cluster;

import java.io.IOException;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.logger.LOG;


public class ClusterClientPool extends BasePooledObjectFactory<NioClient> {
	private final String address;
	private final int port;
	private int count = 0;
	public ClusterClientPool(String address, int port) {
		this.address = address;
		this.port 	 = port;
	}
	
	@Override
	public NioClient create() throws Exception {
		ClusterClient clinet = new ClusterClient(this.address, this.port);
		LOG.application().debug("client : {} / {}", clinet, count++);
		return clinet;
	}

	@Override
	public PooledObject<NioClient> wrap(NioClient client) {
		return new DefaultPooledObject<NioClient>(client);
	}
	
	@Override
    public boolean validateObject(final PooledObject<NioClient> pooledObject) {
		boolean vailedate = false;
		NioClient client = null;
        try {
        	client = pooledObject.getObject();
        	vailedate = client.connect();
		} catch (IOException e) {
			LOG.system().warn("{} : {}", client, e.getMessage());
		}
        return vailedate; 
        		
    }
	@Override
	public void destroyObject(PooledObject<NioClient> pooledObject) throws Exception{
		pooledObject.getObject().close();
	}

}
