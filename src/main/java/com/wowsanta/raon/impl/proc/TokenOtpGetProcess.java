package com.wowsanta.raon.impl.proc;


import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetRequestMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetResponseMessage;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenOtpGetProcess extends AbstractSessionProcess {
	public TokenOtpGetProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new TokenOtpGetResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			TokenOtpGetRequestMessage  request_message  = (TokenOtpGetRequestMessage) getRequest().getMessage();
			log.debug("request : {} ", request_message);
			
			TokenOtpGetResponseMessage response_message = (TokenOtpGetResponseMessage) getResponse().getMessage();
			
			RSTRS data = new RSTRS();
			data.add((byte)0x1,"value1--1");
			data.add((byte)0x2,"value2--2");
			
			response_message.setData(data);
			
			log.debug("response : {} ", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
