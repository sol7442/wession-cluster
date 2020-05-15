package com.wowsanta.raon.impl.proc;


import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.UserDataDelRequestMessage;
import com.wowsanta.raon.impl.message.UserDataDelResponseMessage;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDataDelProcess extends AbstractSessionProcess {
	public UserDataDelProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UserDataDelResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			UserDataDelRequestMessage  request_message  = (UserDataDelRequestMessage) getRequest().getMessage();
			log.debug("request : {}", request_message);
			
			
			UserDataDelResponseMessage response_message = (UserDataDelResponseMessage) getResponse().getMessage();
			response_message.setLot(new INT((int)System.currentTimeMillis()));
			
			log.debug("response : {}", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
