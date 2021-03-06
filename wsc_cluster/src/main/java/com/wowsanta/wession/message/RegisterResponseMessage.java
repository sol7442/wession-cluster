package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterResponseMessage extends ClusterMessage{
	private static final long serialVersionUID = -6548085900988010595L;
	MessageType messageType = MessageType.REGISTER;
	
	int size;
	String message;
}
