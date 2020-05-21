package com.wowsanta.wession.cluster;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.logger.LOG;
import com.wowsanta.wession.message.CreateMessage;
import com.wowsanta.wession.message.DeleteMessage;
import com.wowsanta.wession.message.RegisterRequestMessage;
import com.wowsanta.wession.message.RegisterResponseMessage;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.message.UpdateMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude= {"objectPool"})
public class ClusterNode implements WessionRepository<Wession>, Serializable{
	transient private static final long serialVersionUID = -4821092090124481068L;
	
	String name;
	String address;
	int port;
    
    int maxIdle = 10;
    int minIdle = 2;
    boolean testWhileIdle = true;
    boolean testOnCreate  = true;
    boolean active 		  = false;
    
    transient GenericObjectPool<NioClient> objectPool ;
	public void initialize() {
	    objectPool =  new GenericObjectPool<NioClient>(new ClusterClientPool(address, port));
	    objectPool.setMaxIdle(maxIdle);
	    objectPool.setMinIdle(minIdle);
	    objectPool.setTestWhileIdle(testWhileIdle);
	    objectPool.setTestOnCreate(testOnCreate);

	    LOG.system().info("Cluster Node Initialized : {} ", this);
	}
	@Override
	public void create(Wession session) throws RespositoryException {
		LOG.process().debug("create.cluster.{} : {} ",this.name,session.getKey());
		
		NioClient client = null;
		try {
			client = objectPool.borrowObject();	
			client.write(new CreateMessage(session));
		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(),e);
		}finally {
			objectPool.returnObject(client);
		}
	}
	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}
	@Override
	public void update(Wession s) throws RespositoryException {
		NioClient client = null;
		try {
			client = objectPool.borrowObject();	
			client.write(new UpdateMessage(s));
			
			LOG.process().debug("update.cluster.{} : {} ",this.name,s.getKey());
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
			
			LOG.process().debug("delete.cluster.{} : {} ",this.name,s.getKey());
		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(),e);
		}finally {
			objectPool.returnObject(client);
		}
	}
	public RegisterResponseMessage register(ClusterNode reg_node) throws RespositoryException {
		try {
			
			final ClusterNode this_node = this;
			Future<RegisterResponseMessage> future = Executors.newSingleThreadExecutor().submit(new Callable<RegisterResponseMessage>() {
				@Override
				public RegisterResponseMessage call() throws Exception {
					NioClient client = null;
					try {
						LOG.system().info("cluseter.node.regiester : {} / {}", this_node, reg_node.getName());
						client = objectPool.borrowObject();
						if(client != null) {
							RegisterRequestMessage request_message = new RegisterRequestMessage();
							request_message.setNode(reg_node);
							return (RegisterResponseMessage) client.send(request_message);	
						}else {
							return new RegisterResponseMessage();
						}
						
					} catch (Exception e) {
						LOG.system().info("Register failed : {} / {}", this_node.getName(), reg_node);
						throw new RespositoryException(e.getMessage(),e);
					}finally {
						objectPool.returnObject(client);
					}
				}
			});
			
			try {
				return future.get(3,TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException e) {
				throw new RespositoryException(e.getMessage(), e);
			} catch (TimeoutException e) {
				LOG.system().info("Register failed : {} / {}", this_node.getName(), reg_node);
				return null;
			}
		} catch (Exception e) {
			LOG.system().error(e.getMessage());
			throw new RespositoryException(e.getMessage(), e);
		}
	}
	
	@Override
	public int size() {
		return 0;
	}
	@Override
	public SearchResponseMessage search(SearchRequestMessage r) throws RespositoryException {
		return null;
	}
	public void close() {
		this.objectPool.close();
	}


}
