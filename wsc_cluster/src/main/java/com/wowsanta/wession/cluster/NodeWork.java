package com.wowsanta.wession.cluster;

import com.wowsanta.wession.message.MessageType;
import com.wowsanta.wession.session.Wession;

public class NodeWork {
	final public MessageType type;
	final public Wession     session;
	public NodeWork(MessageType type, Wession session) {
		this.type = type;
		this.session = session;
	}
}
