package com.wowsanta.raon.impl.proc;


import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.UserDataAddRequestMessage;
import com.wowsanta.raon.impl.message.UserDataAddResponseMessage;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDataAddProcess extends AbstractSessionProcess {
	public UserDataAddProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UserDataAddResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			UserDataAddRequestMessage  request_message  = (UserDataAddRequestMessage) getRequest().getMessage();
			log.debug("request : {}", request_message);
			
			
			UserDataAddResponseMessage response_message = (UserDataAddResponseMessage) getResponse().getMessage();
			response_message.setLot(new INT((int)System.currentTimeMillis()));
			
			log.debug("response : {}", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
