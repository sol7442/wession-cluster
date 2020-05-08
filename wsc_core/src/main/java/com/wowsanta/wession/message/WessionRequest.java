package com.wowsanta.wession.message;

import java.io.Serializable;

public interface WessionRequest extends Serializable {
	public MessageType getMessageType();
}
