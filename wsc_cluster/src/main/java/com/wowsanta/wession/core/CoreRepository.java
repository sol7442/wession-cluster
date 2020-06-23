package com.wowsanta.wession.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.index.IndexNode;
import com.wowsanta.wession.index.IndexRepository;
import com.wowsanta.wession.index.IndexSingleFilter;
import com.wowsanta.wession.manager.IndexManager;
import com.wowsanta.wession.message.ResultType;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

public class CoreRepository implements WessionRepository<Wession> {

	int capacity;
	transient protected ConcurrentMap<String,Wession> core = new ConcurrentHashMap<>();
	
	public boolean initialize() {
		if(capacity == 0) {
			capacity = Integer.MAX_VALUE;
		}
		core = new ConcurrentHashMap<String, Wession>();
		return true;
	}
	
	@Override
	public void create(Wession session) throws RespositoryException {
		if(session == null) {
			throw new RespositoryException("NULL VALUE");
		}
		Wession old_session = null;
		try {
			old_session = core.get(session.getKey());
			if(old_session == null) {
				core.put(session.getKey(),session);
			}else {
				LOG.process().warn("old session : {} / ", old_session, session);
			}
		}catch (Exception e) {
			throw e;
		}finally {
			LOG.process().debug("create.core : {} / {}", session.getKey(), core.size());
		}
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
	public SearchResponseMessage<Wession> search(SearchRequestMessage request)throws RespositoryException{
		SearchResponseMessage<Wession> response = new SearchResponseMessage<Wession>();
		try {
			Method order_key = IndexManager.getInstance().getKeyMethod(request.getOrderKey());
			
			Collection<Wession> core_list = core.values();
			
			List<Wession> wession_list = core_list.stream()
			.sorted(new Comparator<Wession>() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				public int compare(Wession o1, Wession o2) {
					try {
						if(o2 == null) return 1;
						if(o1 == null) return -1;
						
						Comparable value1 = (Comparable) order_key.invoke(o1);
						Comparable value2 = (Comparable) order_key.invoke(o2);

						if(value2 == null) return 1;
						if(value1 == null) return 2;
						
						return  value1.compareTo(value2);
						
					} catch (Exception e) {
						LOG.application().error("{}",e.getMessage(), e);
						return 0;	
					}
				}
			})
			.collect(Collectors.toList());
			
			int _index = request.getStartIndex();
			int _count  = request.getCount();
			if(_count > 0){
				int _req_size = _index + _count;
				int _f_index;
				int _t_index;
				if(_req_size >= wession_list.size()) {
					_f_index = _index;
					_t_index = _index + _count - 1;
				}else {
					_f_index = _index;
					_t_index = wession_list.size() - 1;
				}
				response.setResources(wession_list.subList(_f_index, _t_index));	
			}else {
				response.setResources(wession_list);	
			}
			response.setTotalResults(wession_list.size());		
		}catch (Exception e) {
			LOG.application().error("{}:{}",e.getMessage(),request.getFilter(),e);
			throw new RespositoryException(e.getMessage(),e);
		}
		
		return response;
	}
		
		
	@Override
	public int size() {
		return core.size();
	}



}
