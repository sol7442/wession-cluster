package com.wowsanta.wession.cluster;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.wowsanta.wession.message.CreateMessage;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.message.UpdateMessage;
import com.wowsanta.wession.message.WessionMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public abstract class ClusterRepository implements WessionRepository<Wession> {
	transient protected BlockingQueue<WessionMessage> requestQueue ;
	private transient ThreadPoolExecutor executor;
	
	protected Set<ClusterNode> nodes = new HashSet<>();
	
	protected int threadCount;
	public boolean initialize(int size) {
		int core_size  = size + 1;
		int max_size   = core_size * 3;
		int queue_size = core_size * 5;
		
		executor = new ThreadPoolExecutor(core_size, max_size, 10,TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queue_size));
		
		return true;
	}
	
	public abstract class NodeWorker implements Runnable{
		public void run() {
			for (ClusterNode node : nodes) {
				if(node.isActive()) {
					try {
						work(node);
					} catch (RespositoryException e) {
						log.error(e.getMessage(), e);
						node.setActive(false);
					}
				}
			}
		}
		public abstract void work(ClusterNode node)throws RespositoryException ;
	}
	@Override
	public void create(Wession session) throws RespositoryException {
		log.debug("cluster.create : {} ", session);
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
	public void update(Wession s) throws RespositoryException {
		try {
			executor.execute(new NodeWorker() {
				public void work(ClusterNode node) throws RespositoryException {
					node.update(s);
				}
			});

		} catch (Exception e) {
			throw new RespositoryException(e.getMessage(), e);
		}
		log.debug("cluster.update : {} ", s.getKey());
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
		log.debug("cluster.delete : {} ", s.getKey());
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
