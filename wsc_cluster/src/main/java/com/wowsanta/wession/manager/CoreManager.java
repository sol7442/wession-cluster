package com.wowsanta.wession.manager;

import com.wowsanta.wession.core.CoreRepository;

public class CoreManager extends CoreRepository {
	private static CoreManager instance = null;
	private boolean initialized = false;
	
	public static CoreManager getInstance() {
		if(instance == null) {
			instance = new CoreManager();
		}
		
		return instance;
	}
	
	public boolean initialize() {
		if(initialized == false) {
			initialized = true;
		}
		return initialized;
	}
}
