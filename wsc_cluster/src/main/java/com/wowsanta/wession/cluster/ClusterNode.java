package com.wowsanta.wession.cluster;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.wowsanta.wession.message.SearchRequest;
import com.wowsanta.wession.message.SearchResponse;
import com.wowsanta.wession.message.WessionRequest;
import com.wowsanta.wession.message.WessionResponse;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;
import com.wowsanta.wession.session.WessionRepository;

import lombok.Data;

@Data
public class ClusterNode implements WessionRepository<Wession>{
	String name;
    
	GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();


	public void send(WessionRequest request) {
			
	}
	
	public void initialize() {
		 //유휴 커넥션 검사 주기
	    poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L);
	    //풀에 보관중인 커넥션이 유효한지 검사할지 유무 설정
	    poolConfig.setTestWhileIdle(true);
	    //커넥션 최소 개수
	    poolConfig.setMinIdle(4);
	    //커넥션 최대 개수
	    poolConfig.setMaxTotal(50);
	}
	@Override
	public void create(Wession s) throws RespositoryException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Wession read(String key) throws RespositoryException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void update(Wession s) throws RespositoryException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(Wession s) throws RespositoryException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public SearchResponse search(SearchRequest r) throws RespositoryException {
		// TODO Auto-generated method stub
		return null;
	}

}
