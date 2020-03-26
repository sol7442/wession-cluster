package com.wowsanta.wession.session;

import com.wowsanta.wession.message.SearchRequest;
import com.wowsanta.wession.repository.Repository;
import com.wowsanta.wession.session.Session;

public interface SessionRepository<T extends Session> extends Repository<T> {
	
}
