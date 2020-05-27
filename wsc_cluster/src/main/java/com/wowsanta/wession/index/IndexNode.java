package com.wowsanta.wession.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
	
	//ConcurrentMap<Object,Wession> core = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, List<Wession>> cache = new ConcurrentHashMap<String, List<Wession>>();
	private final ConcurrentMap<String, Object> locks = new ConcurrentHashMap<String, Object>();
	
	
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
			LOG.process().info("create.index.{}={} : {} / {} ",keyName,index_key, session.getKey() ,list.size());
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
	public SearchResponseMessage search(SearchRequestMessage r)throws RespositoryException{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int size() {
		int size = 0;
		for (Entry<String,List<Wession>> entry : cache.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
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
}
