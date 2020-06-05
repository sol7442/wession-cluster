package com.wowsanta.wession.manager;

import java.util.Collection;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.core.CoreRepository;
import com.wowsanta.wession.session.Wession;

public class CoreManager extends CoreRepository {
	private static CoreManager instance = null;
	private transient boolean initialized = false;
	
	public static CoreManager getInstance() {
		if(instance == null) {
			instance = new CoreManager();
		}
		
		return instance;
	}
	
	public boolean initialize() {
		try {
			initialized = super.initialize();
		} catch (Exception e) {
			LOG.system().error(e.getMessage(), e);
		}finally {
			LOG.system().info("initialized : {}-{} ", this.getClass().getName(), initialized);
		}
		return initialized;
	}

	public Collection<Wession> values() {
		return core.values();
	}	
}
