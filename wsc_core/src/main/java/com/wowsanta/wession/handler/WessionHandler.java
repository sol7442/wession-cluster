package com.wowsanta.wession.handler;

import com.wowsanta.wession.repository.Repository;
import com.wowsanta.wession.session.Wession;

public interface WessionHandler <T extends Wession> extends Repository<T> {
	public void exception(T s, Throwable e);
	public void finish(Wession session);
	public HandlerType getType();
}
