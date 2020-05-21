package com.wowsanta.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOG {
	public static Logger system() {
		return LoggerFactory.getLogger("system");
	}
	public static Logger application() {
		return LoggerFactory.getLogger("application");
	}
	public static Logger process() {
		return LoggerFactory.getLogger("process");
	}
}
