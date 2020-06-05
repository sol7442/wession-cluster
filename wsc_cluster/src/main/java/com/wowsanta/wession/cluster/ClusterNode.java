package com.wowsanta.wession.cluster;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.wowsanta.client.nio.NioClient;
import com.wowsanta.logger.LOG;
import com.wowsanta.wession.message.ClusterAckRequestMessage;
import com.wowsanta.wession.message.ClusterAckResponseMessage;
import com.wowsanta.wession.message.ClusterPingRequestMessage;
import com.wowsanta.wession.message.ClusterPingResponseMessage;
import com.wowsanta.wession.message.ClusterSyncRequestMessage;
import com.wowsanta.wession.message.ClusterSyncResponseMessage;
import com.wowsanta.wession.message.CreateMessage;
import com.wowsanta.wession.message.DeleteMessage;
import com.wowsanta.wession.message.MessageType;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.message.UpdateMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;

@Data
public class ClusterNode implements Runnable, WessionRepository<Wession>, Serializable {
	transient private static final long serialVersionUID = -4821092090124481068L;

	String name;
	String address;
	int port;

	boolean initialized = false;
	boolean actived = false;
	boolean started = false;
	int coreSize = 0;

	transient BlockingQueue<ClusterMessage> nodeQueue = new ArrayBlockingQueue<ClusterMessage>(2048);
	transient ExecutorService nodeService = null;
	transient GenericObjectPool<NioClient> objectPool ;
	
	public boolean initialize() {
		if (!initialized) {
			try {
				coreSize = Runtime.getRuntime().availableProcessors();
				
				objectPool = new GenericObjectPool<NioClient>(new ClusterClientPool(address, port));
			    objectPool.setMaxIdle(coreSize*2);
			    objectPool.setMinIdle(1);
			    objectPool.setTestWhileIdle(true);
			    objectPool.setTestOnBorrow(true);
			    objectPool.setTestOnCreate(true);
			    
			    nodeService = Executors.newSingleThreadExecutor();
			    
				initialized = true;
			} catch (Exception e) {
				LOG.system().warn("{}", e.getMessage(), e);
			} finally {
				LOG.system().info("Cluster Node Initialized - {} {}:{} / {}", this.name, this.address, this.port, this.coreSize);
			}
		}

		return initialized;
	}
	
	public synchronized boolean isActived() {
		return this.actived;
	}

	public synchronized void setActived(boolean active) {
		this.actived = active;
	}

	public boolean wakeup() {
		setActived(initialize());
		return isActived();
	}
	
	public void start() {
		if (!this.started) {
			//.newFixedThreadPool(coreSize);
			nodeService.execute(this);
			
			//for (int i = 0; i < coreSize; i++) {
				
			//}
		}
		LOG.system().info("{}/{}-{} : {} ", this.name ,this.actived,this.started, this.coreSize);
	}

	public void stop() throws IOException {
		if (this.started) {
			this.actived = false;
			nodeService.shutdownNow();
		}
		LOG.system().info("{} / {}", this.name, nodeQueue.size());
	}

	public void run() {
		try (ClusterClient client = new ClusterClient(this.address,this.port)){
			started = client.connect();
			while (actived && started) {
				ClusterMessage message = nodeQueue.take();
				client.write(message);
				
//				ClusterClient client = null;
//				try {
//					client = (ClusterClient) objectPool.borrowObject(1000);
//					client.write(message);
//				}finally {
//					if(client != null && client.isOpen()) {
//						objectPool.returnObject(client);
//					}
//				}
			}
		} catch (Exception e) {
			LOG.system().warn(e.getMessage());
		} finally {
			this.actived = this.started = false;
			//this.nodeService.shutdownNow();
			LOG.system().info("{} - finish {} ", this.name, this.nodeQueue.size());
		}
	}

	@Override
	public void create(Wession session) throws RespositoryException {
		LOG.process().debug("create.cluster.{} : {} ", this.name, session.getKey());
		try {
			actived = nodeQueue.offer(new CreateMessage(session), 1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOG.system().error(e.getMessage(), e);
		}
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}

	@Override
	public void update(Wession session) throws RespositoryException {
		LOG.process().debug("update.cluster.{} : {} ", this.name, session.getKey());
		try {
			actived = nodeQueue.offer(new UpdateMessage(session), 1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOG.system().error(e.getMessage(), e);
		}
	}

	@Override
	public void delete(Wession session) throws RespositoryException {
		LOG.process().debug("delete.cluster.{} : {} ", this.name, session.getKey());
		try {
			actived = nodeQueue.offer(new DeleteMessage(session), 1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOG.system().error(e.getMessage(), e);
		}
	}

	public ClusterPingResponseMessage ping(ClusterNode reg_node) throws RespositoryException {
		ClusterPingResponseMessage response_message = null;

		String message = null;
		try (NioClient client = new ClusterClient(this.address, this.port)) {
			client.connect();

			ClusterPingRequestMessage request_message = new ClusterPingRequestMessage();
			request_message.setNode(reg_node);
			response_message = client.send(request_message, ClusterPingResponseMessage.class);

			message = MessageType.PING.toString();

		} catch (Exception e) {
			message = e.getMessage();			
		} finally {
			LOG.system().debug("cluseter.node : {} -> {} : {}", reg_node.getName(), this.getName(), message);
		}

		return response_message;
	}

	public ClusterSyncResponseMessage sync(ClusterNode sync_node) throws RespositoryException {
		ClusterSyncResponseMessage response_message = null;

		String message = null;
		try (NioClient client = new ClusterClient(this.address, this.port)) {
			client.connect();

			ClusterSyncRequestMessage request_message = new ClusterSyncRequestMessage();
			request_message.setNode(sync_node);
			response_message = client.send(request_message, ClusterSyncResponseMessage.class);

			message = MessageType.SYNC.toString();

		} catch (Exception e) {
			message = e.getMessage();
		} finally {
			LOG.system().debug("cluseter.node : {} -> {} : {}", sync_node.getName(), this.getName(), message);
		}

		return response_message;
	}

	public ClusterAckResponseMessage ack(ClusterNode ack_node) throws RespositoryException {
		ClusterAckResponseMessage response_message = null;

		String message = null;
		try (NioClient client = new ClusterClient(this.address, this.port)) {
			client.connect();

			ClusterAckRequestMessage request_message = new ClusterAckRequestMessage();
			request_message.setNode(ack_node);
			response_message = client.send(request_message, ClusterAckResponseMessage.class);

			message = MessageType.ACK.toString();
		} catch (Exception e) {
			message = e.getMessage();
		} finally {
			LOG.system().debug("cluseter.node : {} -> {} : {}", ack_node.getName(), this.getName(), message);
		}

		return response_message;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public SearchResponseMessage search(SearchRequestMessage r) throws RespositoryException {
		return null;
	}

}
