package com.wowsanta.wession;

import java.util.HashSet;
import java.util.Set;

import com.wowsanta.logger.LOG;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.core.CoreRepository;
import com.wowsanta.wession.index.IndexRepository;
import com.wowsanta.wession.index.IndexSingleFilter;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.manager.SyncManager;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

public class WessionCluster implements WessionRepository<Wession> {

	static WessionCluster instance;
	
	CoreRepository    coreRepository;
	ClusterRepository clusterRepository;
	IndexRepository   indexRepository;
	
	Set<WessionRepository<Wession>> repositoris = new HashSet<>();
	
	public static WessionCluster getInstance() {
		if(instance == null) {
			instance = new WessionCluster();
		}
		return instance;
	}
	
	public void setCoreRepository(CoreRepository coreRepository) {
		this.coreRepository = coreRepository;
		this.repositoris.add(coreRepository);	
	}
	public void setIndexRepository(IndexRepository indexRepository) {
		this.indexRepository = indexRepository;
		this.repositoris.add(indexRepository);	
	}
	public void setClusterRepository(ClusterRepository clusterRepository) {
		this.clusterRepository = clusterRepository;
		this.repositoris.add(clusterRepository);	
	}
	
	public boolean initialize() {
		boolean result = false;
		try {
			for (WessionRepository<Wession> repository : repositoris) {
				result = repository.initialize();
			}
			result = SyncManager.getInstance().initialize();
		}catch (Exception e) {
			LOG.system().error(e.getMessage(), e);
		}finally {
				
		}
		return result;
	}
	
	@Override
	public void create(Wession session) throws RespositoryException {
		try {
			for (WessionRepository<Wession> repository : repositoris) {
				repository.create(session);
			}	
		}catch (RespositoryException e) {
			throw e;
		}finally {
			LOG.process().info("{} ({}): {}",session.getKey(),size(), session );	
		}
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		Wession session = null;
		try {
			session = coreRepository.read(key);	
		}catch (RespositoryException e) {
			throw e;
		}finally {
			LOG.process().info("{} : {}",key, session);	
		}
		return session;
	}

	@Override
	public void update(Wession session) throws RespositoryException {
		Wession old_sesion = null;
		try {
			old_sesion = coreRepository.read(session.getKey());
			if(old_sesion != null) {
				for (WessionRepository<Wession> repository : repositoris) {
					repository.update(session);
				}		
			}else {
				LOG.process().warn("old session is null");
			}
		}catch (RespositoryException e) {
			throw e;
		}finally {
			LOG.process().info("{} : {}",session.getKey(), session);
		}
	}

	@Override
	public void delete(Wession session) throws RespositoryException {
		Wession old_sesion = null;
		try {
			old_sesion = coreRepository.read(session.getKey());
			if(old_sesion != null) {
				for (WessionRepository<Wession> repository : repositoris) {
					repository.delete(session);
				}		
			}else {
				LOG.process().warn("old session is null");
			}
		}catch (RespositoryException e) {
			throw e;
		}finally {
			LOG.process().info("{} : {}",session.getKey(), session);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SearchResponseMessage search(SearchRequestMessage request)throws RespositoryException{
		SearchResponseMessage<Wession> response = null;
		IndexSingleFilter filter = new IndexSingleFilter();
		filter.parse(request.getFilter());
		
		try {
			if(indexRepository.isIndexKey(filter)) {
				response = indexRepository.search(request);
			}else {
				response = coreRepository.search(request);
			}
		}catch (RespositoryException e) {
			throw e;
		}finally {
			LOG.process().debug("{} : {}/{}",request,  response.getResources().size(), response.getTotalResults());		
		}
		return response;
	}
	
	@Override
	public int size() {
		return coreRepository.size();
	}
	
	public void stop() {
		ClusterManager.getInstance().stop();
	}

	public void start() throws ServerException {
		ClusterManager.getInstance().start();
	}





	

}
