package com.wowsanta.wession.message;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterResponseMessage extends WessionMessage{
	private static final long serialVersionUID = -6548085900988010595L;
	MessageType messageType = MessageType.REGISTER;
	
	int size;
	String message;
}
