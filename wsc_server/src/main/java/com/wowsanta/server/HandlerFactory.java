package com.wowsanta.server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerFactory {
	public Handler newHandlerInstance(String handlerClass) throws ServerException {
		try {
			return (Handler) Class.forName(handlerClass).newInstance();
		} catch (Exception e) {
			throw new ServerException("Handler Instance Error", e);
		}
	}
}
