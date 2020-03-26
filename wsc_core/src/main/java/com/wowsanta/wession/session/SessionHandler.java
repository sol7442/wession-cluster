package com.wowsanta.wession.session;

import com.wowsanta.wession.repository.Repository;

public interface SessionHandler <T extends Session> extends Repository<T> {
	public void exception(T s, Throwable e);
	public void finish(Session session);
	public HandlerType getType();
}
