package com.wowsanta.raon.impl.proc;

import java.util.Date;

import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.VaildateRequestMessage;
import com.wowsanta.raon.impl.message.VaildateResponseMessage;
import com.wowsanta.server.ServerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VaildateProcess extends AbstractSessionProcess {
	public VaildateProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new VaildateResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			VaildateRequestMessage  request_message  = (VaildateRequestMessage) getRequest().getMessage();
			log.debug("request : {} ", request_message);
			
			
			VaildateResponseMessage response_message = (VaildateResponseMessage) getResponse().getMessage();
			
			response_message.setLat(new INT((int)new Date().getTime())) ;
			response_message.setLot(new INT((int)System.currentTimeMillis()));
			
			RSTRS data = new RSTRS();
			data.add((byte)0x0,request_message.getData().get(0x00));
					
			response_message.setData(data);
			
			log.debug("response : {} ", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
