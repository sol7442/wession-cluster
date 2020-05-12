package com.wowsanta.wession.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wowsanta.wession.message.SearchMessage;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class IndexNode implements WessionRepository<Wession>{
	String keyName;
	Method keyMethod;
	
	//ConcurrentMap<Object,Wession> core = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, List<Wession>> cache = new ConcurrentHashMap<String, List<Wession>>();
	private final ConcurrentMap<String, Object> locks = new ConcurrentHashMap<String, Object>();
	
	
	@Override
	public void create(Wession s) throws RespositoryException {
		if(s == null) {
			throw new RespositoryException("NULL VALUE");
		}
		
		String index_key = getIndexKeyValue(s);
		synchronized(getLock(index_key)){
			List<Wession> list=cache.get(index_key);
			if(list == null){
				list = new ArrayList<Wession>();
			}
			list.add(s);
			cache.put(index_key, list);
			log.debug("index.create.{}.{}.{} ", keyName,index_key,s.getKey());

		}
	}
	public List<Wession> list(String index_key) throws RespositoryException {
		log.debug("index.list.{}.{}={} ", keyName,index_key,cache.get(index_key).size());
		return cache.get(index_key);
	}
	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}
	@Override
	public void update(Wession s) throws RespositoryException {
		String index_key = getIndexKeyValue(s);
		synchronized(getLock(index_key)){
			List<Wession> list=cache.get(index_key);
			for (Wession wession : list) {
				if(wession.getKey().equals(s.getKey())){
					list.remove(wession);
					list.add(s);
					break;
				}
			}
		}
		log.debug("index.update.{} : {} ", keyName, index_key);		
	}
	@Override
	public void delete(Wession s) throws RespositoryException {
		String index_key = getIndexKeyValue(s);
		synchronized(getLock(index_key)){
			List<Wession> list=cache.get(index_key);
			for (Wession wession : list) {
				if(wession.getKey().equals(s.getKey())){
					list.remove(wession);
					if(list.size() == 0) {
						cache.remove(index_key);
						log.debug("index.remove.cache.{} : {} ", keyName, index_key);		
					}
					break;
				}
			}
		}
		log.debug("index.remove.{} : {} ", keyName, index_key);		
	}
	@Override
	public SearchResponse search(SearchMessage r)throws RespositoryException{
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
