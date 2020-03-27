package com.wowsanta.server.nio;

import java.io.IOException;
import java.nio.channels.Selector;

import javax.swing.text.TabableView;

import com.wowsanta.server.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class NioService implements Service, Runnable {
	String name;
	transient Selector selector;
	
	public NioService(String name) {
		this.name = name;
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		try {
			while(true) {
				selector.select();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			log.info("finish server : {}",this );
		}
	}
}
