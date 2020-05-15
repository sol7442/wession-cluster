package com.wowsanta.wession.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.message.WessionMessage;
import com.wowsanta.wession.message.WessionResponse;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoreRepository implements WessionRepository<Wession> {

	ConcurrentMap<String,Wession> core = new ConcurrentHashMap<>();
	
	@Override
	public void create(Wession s) throws RespositoryException {
		if(s == null) {
			throw new RespositoryException("NULL VALUE");
		}
		
		core.putIfAbsent(s.getKey(),s);
		log.debug("core.create : {} ", s.getKey());
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		log.debug("core.read : {} ", key);
		return core.get(key);
	}

	@Override
	public void update(Wession s) throws RespositoryException {
		core.put(s.getKey(),s);
		log.debug("core.update : {} ", s.getKey());
	}

	@Override
	public void delete(Wession s) throws RespositoryException {
		log.debug("core.delete : {} ", s.getKey());
		core.remove(s.getKey());
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
