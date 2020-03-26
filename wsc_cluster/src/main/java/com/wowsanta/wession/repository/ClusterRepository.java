package com.wowsanta.wession.repository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.wowsanta.wession.message.SearchRequest;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.session.Session;
import com.wowsanta.wession.session.SessionHandler;
import com.wowsanta.wession.session.SessionRepository;

import lombok.Data;

@Data
public class ClusterRepository implements SessionRepository<Session>{
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    
	private Set<SessionHandler<Session>> handlers = new HashSet<>();
	private Map<String,Session> sessions = new ConcurrentHashMap<>();
	
	private ThreadPoolExecutor executor;
			
//	new ThreadPoolExecutor(10, // core size
//		    50, // max size
//		    10*60, // idle timeout
//		    TimeUnit.SECONDS,
//		    new ArrayBlockingQueue<Runnable>(20));
	
	private abstract class SessionTask implements Runnable{
		protected final Session session;
		public SessionTask(Session session) {
			this.session = session;
		}
		
		@Override
		public void run() {
			for (SessionHandler<Session> handler : handlers) {
				try {
					handling(handler);
				} catch (RespositoryException e) {
					handler.exception(session, e);
				} finally {
					handler.finish(session);
				}
			}
		}
		abstract void handling(SessionHandler<Session> handle)throws RespositoryException;
	}
	private class SesionCreate extends SessionTask{
		public SesionCreate(Session session) {
			super(session);
		}

		@Override
		void handling(SessionHandler<Session> handle) throws RespositoryException {
			handle.create(this.session);
		}
	}
	
	@Override
	public void create(Session session) throws RespositoryException {
		executor.execute(new SesionCreate(session));
	}

	@Override
	public Session read(String key) throws RespositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Session s) throws RespositoryException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Session s) throws RespositoryException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearchResponse search(SearchRequest r) throws RespositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}
