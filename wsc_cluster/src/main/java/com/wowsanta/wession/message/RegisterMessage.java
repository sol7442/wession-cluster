package com.wowsanta.wession.message;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterMessage extends WessionMessage{
	private static final long serialVersionUID = -6548085900988010595L;
	MessageType messageType = MessageType.REGISTER;
	
	String name;
	String address;
	int port;
	
	int size;
	
}
