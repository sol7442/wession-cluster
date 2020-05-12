package com.wowsanta.wession.message;

import java.util.List;

import com.wowsanta.server.Connection;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.session.Wession;

import lombok.Data;

@Data
public class SearchResponse implements WessionResponse {
	ResultType resultType;

	private int totalResults;
	private int itemsPerPage;
	private int startIndex;
	private List<Wession> resources;
	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setConnection(Connection conn) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void write() throws ServerException {
		// TODO Auto-generated method stub
		
	}
	
	
}
