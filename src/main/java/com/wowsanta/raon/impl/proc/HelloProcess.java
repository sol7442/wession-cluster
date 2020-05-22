package com.wowsanta.raon.impl.proc;


import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.HellowRequestMessage;
import com.wowsanta.raon.impl.message.HellowResonseMessage;
import com.wowsanta.server.ServerException;

public class HelloProcess extends AbstractSessionProcess {
	public HelloProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new HellowResonseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			
			HellowRequestMessage request_message = (HellowRequestMessage) getRequest().getMessage();
			HellowResonseMessage response_message = (HellowResonseMessage) getResponse().getMessage();

			LOG.process().info("request  : {} ", request_message);
			
			response_message.setCode(new INT(0));

			LOG.process().info("reponse : {} ", response_message);
		} catch (Exception e) {
			LOG.process().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
