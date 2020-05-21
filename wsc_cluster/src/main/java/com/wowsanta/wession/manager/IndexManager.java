package com.wowsanta.wession.manager;

import com.wowsanta.wession.index.IndexRepository;

public class IndexManager extends IndexRepository {
	private static IndexManager instance = null;
	
	public static IndexManager getInstance() {
		if(instance == null) {
			instance = new IndexManager();
		}
		return instance;
	}

	public static void setInstance(IndexManager indexManager) {
		if(instance == null) {
			instance = indexManager;
		}
	}
}
