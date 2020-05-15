package com.wowsanta.wession.message;

import com.wowsanta.wession.session.Wession;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CreateMessage extends WessionMessage {
	private static final long serialVersionUID = -6548085900988010595L;
	MessageType messageType = MessageType.CREATE;
	private Wession wession;
	
	public CreateMessage(Wession wession) {
		this.wession = wession;
	}
}
