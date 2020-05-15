package com.wowsanta.wession.message;

import java.util.List;

import com.wowsanta.wession.session.Wession;


public class SearchResponse implements WessionResponse {
	ResultType resultType;

	private int totalResults;
	private int itemsPerPage;
	private int startIndex;
	private List<Wession> resources;
	@Override
	public ResultType getResultType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
