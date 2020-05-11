package com.wowsanta.wession.cluster;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.wowsanta.server.nio.NioServer;
import com.wowsanta.wession.handler.WessionHandler;
import com.wowsanta.wession.message.MessageType;
import com.wowsanta.wession.message.SearchRequest;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.message.WessionRequest;
import com.wowsanta.wession.message.WessionResponse;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class ClusterRepository extends NioServer implements WessionRepository<Wession> {
//	private transient final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//	private transient final Lock readLock = readWriteLock.readLock();
//	private transient final Lock writeLock = readWriteLock.writeLock();
	
	private transient ThreadPoolExecutor executor;

	private Set<ClusterNode> nodes = new HashSet<>();

	@Override
	public boolean initialize() {
		this.executor = new ThreadPoolExecutor(
				10, // core size
				50, // max size
				10 * 60, // idle timeout
				TimeUnit.SECONDS, 
				new ArrayBlockingQueue<Runnable>(20));

		return super.initialize();
	}


	@Override
	public void create(Wession s) throws RespositoryException {
		//executor.execute(new WessionClusterRequest(MessageType.CREATE, s));
		log.debug("cluster.create : {} ", s.getKey());
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}

	@Override
	public void update(Wession s) throws RespositoryException {
		//executor.execute(new WessionClusterRequest(MessageType.UPDATE, s));
		log.debug("cluster.update : {} ", s.getKey());
	}

	@Override
	public void delete(Wession s) throws RespositoryException {
		//executor.execute(new WessionClusterRequest(MessageType.DELETE, s));
		log.debug("cluster.delete : {} ", s.getKey());
	}

	@Override
	public SearchResponse search(SearchRequest r)throws RespositoryException{
		return null;
	}
	@Override
	public int size() {
		return 0;
	}

}
