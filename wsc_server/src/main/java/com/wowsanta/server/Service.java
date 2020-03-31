package com.wowsanta.server;


public interface Service<T extends Connection> {

	void register(T t);

}
