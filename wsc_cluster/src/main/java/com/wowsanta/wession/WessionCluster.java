package com.wowsanta.wession;

import java.util.HashSet;
import java.util.Set;

import com.wowsanta.wession.cluster.ClusterRepository;
import com.wowsanta.wession.core.CoreRepository;
import com.wowsanta.wession.index.IndexRepository;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("add repository : {}", coreRepository.getClass().getName());
	}
	public void setIndexRepository(IndexRepository indexRepository) {
		this.indexRepository = indexRepository;
		this.repositoris.add(indexRepository);
		log.info("add repository : {}", indexRepository.getClass().getName());
	}
	public void setClusterRepository(ClusterRepository clusterRepository) {
		this.clusterRepository = clusterRepository;
		this.repositoris.add(clusterRepository);
		log.info("add repository : {}", clusterRepository.getClass().getName());
	}
	
	
	@Override
	public void create(Wession s) throws RespositoryException {
		log.debug("creat:{}",s.getKey());
		for (WessionRepository<Wession> repository : repositoris) {
			log.debug("repository :  {} " ,repository);
			repository.create(s);
		}
		log.info("creat:{}",s);
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		log.debug("read:{}",key);
		return coreRepository.read(key);
	}

	@Override
	public void update(Wession s) throws RespositoryException {
		log.debug("update:{}",s.getKey());
		for (WessionRepository<Wession> repository : repositoris) {
			repository.update(s);
		}
		log.info("update:{}",s);
	}

	@Override
	public void delete(Wession s) throws RespositoryException {
		log.debug("delete:{}",s.getKey());
		for (WessionRepository<Wession> repository : repositoris) {
			repository.delete(s);
		}
		log.info("delete:{}",s);
	}

	@Override
	public SearchResponseMessage search(SearchRequestMessage request)throws RespositoryException{
		SearchResponseMessage response = null;
		if(isIndexSearch(request.getFilter())) {
			response = indexRepository.search(request);
		}else {
			response = coreRepository.search(request);
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
