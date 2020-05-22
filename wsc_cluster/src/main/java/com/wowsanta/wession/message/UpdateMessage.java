package com.wowsanta.wession.message;

import com.wowsanta.wession.cluster.ClusterMessage;
import com.wowsanta.wession.session.Wession;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UpdateMessage extends ClusterMessage {
	private static final long serialVersionUID = 7428925535426149542L;
	MessageType messageType = MessageType.UPDATE;
	private Wession wession;
	
	public UpdateMessage(Wession wession) {
		this.wession = wession;
	}
}
