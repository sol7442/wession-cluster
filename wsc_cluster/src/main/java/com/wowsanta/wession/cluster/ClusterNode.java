package com.wowsanta.wession.cluster;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.server.Response;
import com.wowsanta.wession.message.CreateMessage;
import com.wowsanta.wession.message.DeleteMessage;
import com.wowsanta.wession.message.RegisterMessage;
import com.wowsanta.wession.message.SearchMessage;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.message.UpdateMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(exclude= {"objectPool"})
public class ClusterNode implements WessionRepository<Wession>{
	String name;
	String address;
	int port;
    
    int maxIdle;
    int minIdle;
    boolean testWhileIdle = true;
    boolean testOnCreate  = true;

    transient GenericObjectPool<NioClient> objectPool ;
	public void initialize() {
	    objectPool =  new GenericObjectPool<NioClient>(new ClusterClientPool(address, port));
	    objectPool.setMaxIdle(maxIdle);
	    objectPool.setMinIdle(minIdle);
	    objectPool.setTestWhileIdle(testWhileIdle);
	    objectPool.setTestOnCreate(testOnCreate);
	    
	    log.info("Cluster Node Initialized : {} ", this);
	}
	@Override
	public void create(Wession s) throws RespositoryException {
		NioClient client = null;
		try {
			client = objectPool.borrowObject();	
			client.write(new CreateMessage(s));
		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(),e);
		}finally {
			objectPool.returnObject(client);
		}
	}
	@Override
	public Wession read(String key) throws RespositoryException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void update(Wession s) throws RespositoryException {
		NioClient client = null;
		try {
			client = objectPool.borrowObject();	
			client.write(new UpdateMessage(s));
		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(),e);
		}finally {
			objectPool.returnObject(client);
		}
		
	}
	@Override
	public void delete(Wession s) throws RespositoryException {
		NioClient client = null;
		try {
			client = objectPool.borrowObject();	
			client.write(new DeleteMessage(s));
		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(),e);
		}finally {
			objectPool.returnObject(client);
		}
	}
	public RegisterMessage register() throws RespositoryException {
		try {
			final ClusterNode this_node = this;
			
			Future<RegisterMessage> future = Executors.newSingleThreadExecutor().submit(new Callable<RegisterMessage>() {
				@Override
				public RegisterMessage call() throws Exception {
					NioClient client = null;
					try {
						client = objectPool.borrowObject();
						
						RegisterMessage request_message = new RegisterMessage();
						request_message.setAddress(this_node.getAddress());
						request_message.setPort(this_node.getPort());
						request_message.setName(this_node.getName());
						
						return (RegisterMessage) client.send(request_message);
					} catch (Exception e) {
						log.error("{}",e.getMessage(),e);
						throw new RespositoryException(e.getMessage(),e);
					}finally {
						objectPool.returnObject(client);
					}
				}
			});
			
			return future.get(3,TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RespositoryException(e.getMessage(), e);
		}
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public SearchResponse search(SearchMessage r) throws RespositoryException {
		// TODO Auto-generated method stub
		return null;
	}
	public void close() {
		this.objectPool.close();
	}


}
