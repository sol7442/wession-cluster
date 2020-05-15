package com.wowsanta.raon.impl.proc;


import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.UnregisterRequestMessage;
import com.wowsanta.raon.impl.message.UnregisterResonseMessage;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnregisterProcess extends AbstractSessionProcess {
	public UnregisterProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UnregisterResonseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			UnregisterRequestMessage request_message = (UnregisterRequestMessage) getRequest().getMessage();
			log.debug("request : {} ", request_message);
			
			UnregisterResonseMessage response_message = (UnregisterResonseMessage) getResponse().getMessage();
			log.debug("response : {} ", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
