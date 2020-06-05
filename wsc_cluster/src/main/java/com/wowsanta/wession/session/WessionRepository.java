package com.wowsanta.wession.session;

import com.wowsanta.wession.repository.Repository;
import com.wowsanta.wession.session.Wession;

public interface WessionRepository<T extends Wession> extends Repository<T> {
	public boolean initialize();
}
