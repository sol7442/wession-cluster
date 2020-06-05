package com.wowsanta.wession.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;

@Data
public class IndexNode implements WessionRepository<Wession>{
	String keyName;
	Method keyMethod;
	
	private final ConcurrentMap<String, List<Wession>> cache = new ConcurrentHashMap<String, List<Wession>>();
	private final ConcurrentMap<String, Object> locks = new ConcurrentHashMap<String, Object>();
	
	@Override
	public boolean initialize() {
		return false;
	}
	@Override
	public void create(Wession session) throws RespositoryException {
		if(session == null) {
			throw new RespositoryException("NULL VALUE");
		}
		
		String index_key = getIndexKeyValue(session);
		synchronized(getLock(index_key)){
			List<Wession> list=cache.get(index_key);
			if(list == null){
				list = new ArrayList<Wession>();
			}
			list.add(session);
			cache.put(index_key, list);
			LOG.process().debug("create.index.{}={} : {} / {} ",keyName,index_key, session.getKey() ,list.size());
		}
	}
	public List<Wession> list(String index_key) throws RespositoryException {
		List<Wession> list = cache.get(index_key);
		if(list == null) {
			list = new ArrayList<>();
		}
		return list;
	}
	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}
	@Override
	public void update(Wession session) throws RespositoryException {

	}
	@Override
	public void delete(Wession session) throws RespositoryException {
		String index_key = getIndexKeyValue(session);
		synchronized(getLock(index_key)){
			List<Wession> list=cache.get(index_key);
			for (Wession wession : list) {
				if(wession.getKey().equals(session.getKey())){
					list.remove(wession);
					if(list.size() == 0) {
						cache.remove(index_key);
					}
					break;
				}
			}
		}
		LOG.process().debug("delete.index.{}={} : {} ",keyName,index_key, session.getKey());
	}
	@Override
	public SearchResponseMessage<Wession> search(SearchRequestMessage r)throws RespositoryException{
		// TODO Auto-generated method stub
		return null;
	}
	
	public int totalSize() {
		int size = 0;
		for (Entry<String,List<Wession>> entry : cache.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}
	@Override
	public int size() {
		return cache.size();
	}
	public int size(String value_key) {
		List<Wession> list = cache.get(value_key);
		if(list != null) {
			return list.size();
		}
		return 0;
	}
	

	
	private String getIndexKeyValue(Wession s) throws RespositoryException{
		String index_key = null;
		try {
			index_key = (String) keyMethod.invoke(s);
		} catch (Exception e) {
			throw new RespositoryException(keyMethod.getName() + " method invoke error.",e);
		}
		return index_key;
	}
	
	private Object getLock(final String key){
		final Object object=new Object();
	    Object lock=locks.putIfAbsent(key, object);
	    if(lock == null){
	      lock=object;
	    }
	    return lock;
	}
	public List<Wession> filter(SingleFilter filter, Method orderKey) {
		List<Wession> result_list = filter.op.filter(this.cache, filter.getValue());
		
		Collections.sort(result_list, new Comparator<Wession>() {
			@Override
			public int compare(Wession o1, Wession o2) {
				try {
					Object value1 = orderKey.invoke(o1);
					Object value2 = orderKey.invoke(o2);
					
					return value1.equals(value2) ? 1:0 ;
					
				} catch (Exception e) {
					return 0;	
				}
			}
		});
		
		return result_list;
	}
}
