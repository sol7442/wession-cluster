package com.wowsanta.raon.impl.proc;


import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.UserDataGetRequestMessage;
import com.wowsanta.raon.impl.message.UserDataGetResponseMessage;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDataGetProcess extends AbstractSessionProcess {
	public UserDataGetProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UserDataGetResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			UserDataGetRequestMessage  request_message  = (UserDataGetRequestMessage) getRequest().getMessage();
			log.debug("request : {}", request_message);
			
			
			UserDataGetResponseMessage response_message = (UserDataGetResponseMessage) getResponse().getMessage();
			response_message.setLot(new INT((int)System.currentTimeMillis()));
			response_message.setData(new STR("MBRS_MB_KNCD-0*RWRD_USE_YN - by WessionSession Server."));
			
			log.debug("response : {}", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
