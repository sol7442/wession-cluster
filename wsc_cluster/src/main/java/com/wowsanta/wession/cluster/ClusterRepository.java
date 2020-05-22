package com.wowsanta.wession.cluster;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ClusterRepository implements WessionRepository<Wession> {
	transient protected BlockingQueue<ClusterMessage> requestQueue ;
	
	private transient ThreadPoolExecutor executor;
	private transient boolean initialized = false;
	
	protected ConcurrentMap<String, ClusterNode> nodeMap = new ConcurrentHashMap<>();
	protected int threadCount;
	
	public boolean initialize()  {
		if(initialized == false) {
			int core_size  = threadCount + 1;
			int max_size   = core_size * 3;
			int queue_size = core_size * 5;
			
			this.executor = new ThreadPoolExecutor(core_size, max_size, 10,TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queue_size));	
			
			initialized = true;			
		}
		
		
		return true;
	}
	
	public abstract class NodeWorker implements Runnable{
		public void run() {
			Set<Entry<String, ClusterNode>> node_set =  nodeMap.entrySet();
			for (Entry<String, ClusterNode> node_entry : node_set) {
				ClusterNode node = node_entry.getValue();
				LOG.process().debug("work : {}/{}",node.getName(),node.isActive());

				if(node.isActive()) {
					try {
						work(node);
					} catch (RespositoryException e) {
						LOG.process().error(e.getMessage(), e);
						node.setActive(false);
					}
				}
				
			}
		}
		public abstract void work(ClusterNode node)throws RespositoryException ;
	}
	@Override
	public void create(Wession session) throws RespositoryException {
		LOG.process().debug("create.cluster : {} ", session.getKey());
		try {
			executor.execute(new NodeWorker() {
				@Override
				public void work(ClusterNode node) throws RespositoryException {
					node.create(session);
				}
			});
		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(), e);
		}
		
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}

	@Override
	public void update(Wession session) throws RespositoryException {
		LOG.process().debug("update : {} ", session.getKey());
		try {
			executor.execute(new NodeWorker() {
				public void work(ClusterNode node) throws RespositoryException {
					node.update(session);
				}
			});

		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(Wession s) throws RespositoryException {
		try {
			executor.execute(new NodeWorker() {
				public void work(ClusterNode node) throws RespositoryException {
					node.delete(s);
				}
			});

		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(), e);
		}
		LOG.process().debug("delete : {} ", s.getKey());
	}

	@Override
	public SearchResponseMessage search(SearchRequestMessage r)throws RespositoryException{
		return null;
	}
	@Override
	public int size() {
		return 0;
	}

}
