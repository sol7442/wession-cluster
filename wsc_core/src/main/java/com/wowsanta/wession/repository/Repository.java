package com.wowsanta.wession.repository;

import com.wowsanta.wession.message.SearchRequest;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.session.Session;

public interface Repository<T extends Session> {
	public void create(T s) throws RespositoryException;
	public T read(String key)throws RespositoryException;
	public void update(T s)throws RespositoryException;
	public void delete(T s)throws RespositoryException;
	public SearchResponse search(SearchRequest r)throws RespositoryException;
}
