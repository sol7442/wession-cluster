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
			LOG.process().debug("create.index.{}={} : {} / {} ",keyName,index_key, cache.size(),list.size());
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
	public List<Wession> filter(IndexSingleFilter filter, Method order_key) {
		List<Wession> result_list = filter.operator.filter(this.cache, filter.getValue());
		if(result_list != null) {
			Collections.sort(result_list, new Comparator<Wession>() {
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
			});			
		}
		return result_list;
	}
}
