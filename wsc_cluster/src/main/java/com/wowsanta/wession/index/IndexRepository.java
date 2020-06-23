package com.wowsanta.wession.index;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.message.ResultType;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IndexRepository implements WessionRepository<Wession> {

	transient ConcurrentMap<String,IndexNode> nodeMap = new ConcurrentHashMap<>();
	transient Class<?> wession_class = null;
	
	private transient boolean initialized = false;
	
	private List<String> keyList = new ArrayList<String>();
	private String wessionClassName;
	protected int threadCount;
	
	public boolean initialize() {
		if(initialized == false) {
			try {
				wession_class = Class.forName(wessionClassName);
			} catch (ClassNotFoundException e) {
				LOG.system().error(e.getMessage(),e);
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
				nodeMap.put(key_name, node);
			}
			initialized = true;
		}
		return true;
	}
	
	
	public abstract class NodeWorker implements Runnable{
		public void run() {
			Set<Entry<String, IndexNode>> node_set =  nodeMap.entrySet();
			for (Entry<String, IndexNode> node_entry : node_set) {
				IndexNode node = node_entry.getValue();
				try {
					work(node);
				} catch (RespositoryException e) {
					LOG.process().error(e.getMessage(), e);
				}
			}
		}
		public abstract void work(IndexNode node)throws RespositoryException ;
	}
	
	
	@Override
	public void create(Wession session) throws RespositoryException {
		LOG.process().debug("create.index : {} ", session.getKey());
		for (String key_name : keyList) {
			IndexNode node = nodeMap.get(key_name);
			node.create(session);
		}
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
		LOG.process().debug("delete.index : {} ", session.getKey());
		for (String key_name : keyList) {
			IndexNode node = nodeMap.get(key_name);
			node.delete(session);
		}
	}

	@Override
	public SearchResponseMessage<Wession> search(SearchRequestMessage request)throws RespositoryException{
		SearchResponseMessage<Wession> response = new SearchResponseMessage<Wession>();
		try {

			IndexSingleFilter filter = new IndexSingleFilter();
			filter.parse(request.getFilter());
			
			Method order_key = getKeyMethod(request.getOrderKey());
			
			LOG.process().debug("index.search.filter : {}-{} ", filter);
			IndexNode node = nodeMap.get(filter.getKey());
			if(node != null) {
				List<Wession> wession_list = node.filter(filter,order_key);
				if(wession_list != null) {
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
				}else {
					response.setResources(new ArrayList<>());	
					response.setTotalResults(0);	
				}
			}else {
				throw new RespositoryException("node not found : " + filter);
			}
			response.setResultType(ResultType.SUCCESS);
		}catch (Exception e) {
			throw new RespositoryException(e.getMessage(), e);
		}
		return response;
	}

	public int size(String key_name, String key_value) {
		IndexNode node = nodeMap.get(key_name);
		if(node != null) {
			return node.size(key_value);
		}
		return 0;
	}
	public int size(String key_name) {
		IndexNode node = nodeMap.get(key_name);
		if(node != null) {
			return node.size();
		}
		return 0;
	}
	
	@Override
	public int size() {
		int size = 0 ;
		try {
			IndexNode node = nodeMap.get(this.keyList.get(0));
			size = node.totalSize();
		}catch (Exception e) {
			LOG.application().error(e.getMessage());
		}
		return size;
	}
	public Method getKeyMethod(String key_name) throws RespositoryException {
		Method key_method = null;
		String method_name = toMethodName(key_name);
		try {
			key_method = wession_class.getMethod(method_name, (Class<?>[])null);
		} catch (Exception e) {
			throw new RespositoryException("["+ method_name +"]" + " method not found.",e);
		} 
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

	public boolean isIndexKey(IndexSingleFilter filter) {
		if(filter.key != null) {
			return this.keyList.contains(filter.key);			
		}
		return false;
	}

}
