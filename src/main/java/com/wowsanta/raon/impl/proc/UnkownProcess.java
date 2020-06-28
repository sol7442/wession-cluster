package com.wowsanta.raon.impl.proc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.AccountDelRequestMessage;
import com.wowsanta.raon.impl.message.AccountDelResonseMessage;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.UnkownRequestMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;

public class UnkownProcess extends AbstractSessionProcess {
	private final int command;
	
	public UnkownProcess(int command) {
		this.command = command;
		setRequest(new SessionRequest(new UnkownRequestMessage()));
	}
	@Override
	public void porcess() throws ServerException {

		UnkownRequestMessage request_message  = (UnkownRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		try {
			LOG.application().debug("request  : {} ", request_message);
			
			LOG.application().warn("message  : {} ", new String(request_message.toBytes(),"euc-kr"));
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_UNKNOWN.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRNET_PROTOCOL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRNET_PROTOCOL.getMessage() + "Unkown Command : " + this.command));
			
			response_message = erro_messge;
	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		finally {
			LOG.application().debug("response : {} ", response_message);
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
		}
	}

}
