package com.wowsanta.raon.impl.policy;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.manager.LifeCycleManager;
import com.wowsanta.wession.manager.PolicyManager;

public class RaonLifeCycleManager extends LifeCycleManager {

	@Override
	public void run() {
		int collected = 0;
		try {
			RaonSessionPolicy policy = (RaonSessionPolicy) PolicyManager.getInstance().getPolicy();
			if(policy != null) {
				
				String filter = "modifyTime " + policy.maxInactiveInterval;
				
				
			}			
		}catch (Exception e) {
			
		}finally {
			LOG.process().info("collected session : {}", collected);
		}
	}
}
