package com.wowsanta.raon.impl.proc;


import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.HelloRequestMessage;
import com.wowsanta.raon.impl.message.HelloResonseMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;

public class HelloProcess extends AbstractSessionProcess {
	public HelloProcess() {
		setRequest(new SessionRequest(new HelloRequestMessage()));
		//setResponse(new SessionResponse(new HellowResonseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		
		RaonSessionMessage response_message = null;
		try {
			HelloRequestMessage request_message = (HelloRequestMessage) getRequest().getMessage();
			LOG.application().info("request  : {} ", request_message);
			
			HelloResonseMessage hello_message = new HelloResonseMessage();
			hello_message.setCode(new INT(0));
			
			response_message = hello_message;
		} catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		}finally {
			LOG.application().info("reponse : {} ", response_message);
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
		}
	}

}
