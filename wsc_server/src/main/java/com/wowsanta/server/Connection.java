package com.wowsanta.server;


public interface Connection extends AutoCloseable{
	int read0() throws ServerException;
	int write0() throws ServerException;
}
