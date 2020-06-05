package com.wowsanta.wession.policy;

import com.wowsanta.wession.session.Wession;

public interface Policy<T extends Wession> {
	public Policy<T> load(String file);
	public void save(String file);
	
	public String toString(boolean pretty);
	public boolean initialize();
}
