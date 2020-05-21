package com.wowsanta.wession.message;


import com.wowsanta.wession.cluster.ClusterNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterRequestMessage extends WessionMessage{
	private static final long serialVersionUID = 6548085900988010595L;
	MessageType messageType = MessageType.REGISTER;
	
	ClusterNode node;
}
