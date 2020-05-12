package com.wowsanta.wession.repository;

import com.wowsanta.wession.message.SearchMessage;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.message.WessionMessage;
import com.wowsanta.wession.message.WessionResponse;
import com.wowsanta.wession.session.Wession;

public interface Repository<T extends Wession> {
	public void create(T s) throws RespositoryException;
	public T read(String key)throws RespositoryException;
	public void update(T s)throws RespositoryException;
	public void delete(T s)throws RespositoryException;
	public int size();
	public SearchResponse search(SearchMessage r)throws RespositoryException;
}
