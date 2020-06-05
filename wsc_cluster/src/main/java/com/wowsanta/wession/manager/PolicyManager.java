package com.wowsanta.wession.manager;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.policy.Policy;

public class PolicyManager {
	private static PolicyManager instance = null;
	
	private String policyFile;
	private String policyClass;
	private transient boolean initialized = false;
	
	transient Policy<?> policy;
	
	public static PolicyManager getInstance() {
		if(instance == null) {
			instance = new PolicyManager();
		}
		return instance;
	}
	public static void setInstance(PolicyManager policyManager) {
		if(instance == null) {
			instance = policyManager;
		}
	}
	public boolean initialize() {
		if(!initialized) {
			try {
				Policy<?> policy = (Policy<?>) Class.forName(policyClass).newInstance();
				this.policy = policy.load(policyFile);
				if(this.policy != null) {
					initialized = this.policy.initialize();
				}
				
			}catch (Exception e) {
				LOG.system().error(e.getMessage(),e);
			}finally {
				LOG.system().info("initialized : {}-{}",policyClass,this.initialized);
				if(this.initialized) {
					LOG.system().info("\n{}",this.policy.toString(true));
				}
			}
		}
		return initialized;
	}
	
	public Policy<?> getPolicy() {
		return this.policy;
	}
	public void setPolicy(Policy<?> policy) {
		this.policy = policy;
	}

	public String getPolicyFile() {
		return policyFile;
	}

	public void setPolicyFile(String policyFile) {
		this.policyFile = policyFile;
	}

	public String getPolicyClass() {
		return policyClass;
	}

	public void setPolicyClass(String policyClass) {
		this.policyClass = policyClass;
	}

	public boolean isInitialized() {
		return initialized;
	}


}
