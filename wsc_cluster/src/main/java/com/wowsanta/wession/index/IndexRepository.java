package com.wowsanta.wession.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class IndexRepository implements WessionRepository<Wession> {

	transient ConcurrentMap<String,IndexNode> indexNodes = new ConcurrentHashMap<>();
	transient Class<?> wession_class = null;
	
	List<String> keyList = new ArrayList<String>();
	String wessionClassName;
	
	public boolean initialize() {
		try {
			wession_class = Class.forName(wessionClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
				
		for (String key_name : keyList) {
			IndexNode node = new IndexNode();
			try {
				Method key_method = getKeyMethod(key_name);
				node.setKeyName(key_name);
				node.setKeyMethod(key_method);
			} catch (RespositoryException e) {
				e.printStackTrace();
			}
			indexNodes.put(key_name, node);
		}
		
		return true;
	}
	
	@Override
	public void create(Wession s) throws RespositoryException {
		for (String key_name : keyList) {
			IndexNode node = indexNodes.get(key_name);
			node.create(s);
		}
		log.debug("index.create : {} ", s.getKey());
	}

	@Override
	public Wession read(String key) throws RespositoryException {
		return null;
	}

	@Override
	public void update(Wession s) throws RespositoryException {
		for (String key_name : keyList) {
			IndexNode node = indexNodes.get(key_name);
			node.update(s);
		}
		log.debug("index.update : {} ", s.getKey());
		
	}

	@Override
	public void delete(Wession s) throws RespositoryException {
		for (String key_name : keyList) {
			IndexNode node = indexNodes.get(key_name);
			node.delete(s);
		}
		log.debug("index.delete : {} ", s.getKey());
	}

	@Override
	public SearchResponseMessage search(SearchRequestMessage request)throws RespositoryException{
		SearchResponseMessage response = new SearchResponseMessage();
		String filter = request.getFilter();
		String filter_key   = getFilterKey(filter);
		String filter_value = getFilterValue(filter);
		
		log.debug("index.search : {}-{} ", filter_key, filter_value);
		IndexNode node = indexNodes.get(filter_key);
		log.debug("index.node : {}-{} ", filter_key, node);
		
		if(node != null) {
			response.setResources(node.list(filter_value));			
		}

		
		return response;
	}
	private String getFilterValue(String filter) {
		return filter.split("=")[1];
	}

	private String getFilterKey(String filter) {
		return filter.split("=")[0];
	}

	@Override
	public int size() {
		return 0;
	}
	private Method getKeyMethod(String key_name) throws RespositoryException {
		Method key_method = null;
		String method_name = toMethodName(key_name);
		try {
			key_method = wession_class.getMethod(method_name, (Class<?>[])null);
		} catch (Exception e) {
			throw new RespositoryException("["+ method_name +"]" + " method not found.",e);
		} 
		log.debug("index.key : {} ", method_name);
		return key_method;
	}

	private String toMethodName(String key_name) throws RespositoryException{
		StringBuffer buffer = new StringBuffer();

		try {
			buffer.append("get");
			buffer.append(key_name.substring(0,1).toUpperCase());
			buffer.append(key_name.substring(1, key_name.length()));
		}catch (Exception e) {
			throw new RespositoryException(key_name + " method make failed.",e);
		}
		return buffer.toString();
	}

}
