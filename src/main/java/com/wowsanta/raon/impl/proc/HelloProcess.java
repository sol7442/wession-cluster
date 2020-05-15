package com.wowsanta.raon.impl.proc;


import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.HellowResonseMessage;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloProcess extends AbstractSessionProcess {
	public HelloProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new HellowResonseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			log.debug("request  : {} ", getRequest().getMessage());
			
			
			HellowResonseMessage response_message = (HellowResonseMessage) getResponse().getMessage();
			response_message.setCode(new INT(0));

			log.debug("response : {} ", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
