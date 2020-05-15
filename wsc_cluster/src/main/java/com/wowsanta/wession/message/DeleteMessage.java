package com.wowsanta.wession.message;

import com.wowsanta.wession.session.Wession;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class DeleteMessage extends WessionMessage {
	private static final long serialVersionUID = -5969700479523482525L;

	MessageType messageType = MessageType.DELETE;
	private Wession wession;

	public DeleteMessage(Wession wession) {
		this.wession = wession;
	}
}
