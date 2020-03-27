package com.wowsanta.server.nio;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.wowsanta.server.Service;

public class ServiceList<T> extends ArrayList<Service<T>> {
	private static final long serialVersionUID = 1L;
	
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    
	private int idx_cur;
	
	public boolean add(Service<T> service) {
		writeLock.lock();
		try {
			return super.add(service);	
		}finally {
			writeLock.unlock();
		}
	}
	public boolean remove(Service<T> service) {
		writeLock.lock();
		try {
			return super.remove(service);	
		}finally {
			writeLock.unlock();
		}
	}
	
	public Service<T> next() {
		readLock.lock();
		try {
			idx_cur++;
			if(idx_cur >= size()) {
				idx_cur = 0;
			}
			return get(idx_cur);
		}finally {
			readLock.unlock();
		}
	}
}
