package com.wowsanta.wession.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

public class CoreRepository implements WessionRepository<Wession> {

	ConcurrentMap<String,Wession> core = new ConcurrentHashMap<>();
	
	public boolean initialize() {
		LOG.system().info("CoreRepository : {} ", true);
		return true;
	}
	
	@Override
	public void create(Wession session) throws RespositoryException {
		if(session == null) {
			throw new RespositoryException("NULL VALUE");
		}
		
		core.putIfAbsent(session.getKey(),session);
		LOG.process().debug("create.core : {} ", session.getKey());
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		LOG.process().debug("core.read : {} ", key);
		return core.get(key);
	}

	@Override
	public void update(Wession session) throws RespositoryException {
		core.put(session.getKey(),session);
		LOG.process().debug("core.update : {} ", session.getKey());
	}

	@Override
	public void delete(Wession session) throws RespositoryException {
		LOG.process().debug("core.delete : {} ", session.getKey());
		core.remove(session.getKey());		
	}

	public List<Wession> list() {
		return new ArrayList<Wession>(core.values());
	}
	
	@Override
	public SearchResponseMessage search(SearchRequestMessage request)throws RespositoryException{
		SearchResponseMessage response = new SearchResponseMessage();
		
		List<Wession> core_list = new ArrayList<Wession>(core.values());
		List<Wession> result_list = new ArrayList<Wession>();
		
		// pagenation 데충 만든듬.--- 짜증..
		int start_idx = request.getStartIndex();
		int count     = request.getCount();
		for(int i= start_idx; i < core.size(); i++) {
			result_list.add(core_list.get(i));
			count--;
			if(count == 0) {
				break;
			}
		}
		
		response.setResources(result_list);
		response.setStartIndex(start_idx);
		response.setItemsPerPage(count);
		response.setTotalResults(core.size());
		
		return response;
	}
	@Override
	public int size() {
		return core.size();
	}



}
