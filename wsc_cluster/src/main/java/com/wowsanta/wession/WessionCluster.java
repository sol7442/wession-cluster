package com.wowsanta.wession;

import java.util.HashSet;
import java.util.Set;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.core.CoreRepository;
import com.wowsanta.wession.index.IndexRepository;
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
		LOG.system().info("{}", coreRepository);
		if(coreRepository.initialize()) {
			this.coreRepository = coreRepository;
			this.repositoris.add(coreRepository);	
		}
	}
	public void setIndexRepository(IndexRepository indexRepository) {
		LOG.system().info("{}", indexRepository);
		if(indexRepository.initialize()) {
			this.indexRepository = indexRepository;
			this.repositoris.add(indexRepository);	
		}
	}
	public void setClusterRepository(ClusterRepository clusterRepository) {
		LOG.system().info("{}", clusterRepository);
		if(clusterRepository.initialize()) {
			this.clusterRepository = clusterRepository;
			this.repositoris.add(clusterRepository);	
		}		
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

	@Override
	public SearchResponseMessage search(SearchRequestMessage request)throws RespositoryException{
		SearchResponseMessage response = null;
		try {
			if(isIndexSearch(request.getFilter())) {
				response = indexRepository.search(request);
			}else {
				response = coreRepository.search(request);
			}
		}catch (RespositoryException e) {
			throw e;
		}finally {
			LOG.process().debug("{} / {}",request, response);		
		}
		return response;
	}
	
	@Override
	public int size() {
		return coreRepository.size();
	}
	
	private boolean isIndexSearch(String filter) {
		if(filter == null || filter.startsWith("key"))
			return false;
		else
			return true;
	}

}
