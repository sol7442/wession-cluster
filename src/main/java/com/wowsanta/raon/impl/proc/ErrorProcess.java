package com.wowsanta.raon.impl.proc;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;

public class ErrorProcess extends AbstractSessionProcess{

	Throwable e;
	public ErrorProcess(Throwable e) {
		this.e = e;
	}

	@Override
	public void porcess() throws ServerException {
		RaonSessionMessage response_message = null;
		try {
			
			LOG.application().error(e.getMessage(),e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_ERROR.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(e.getMessage()));
			
			response_message = erro_messge;;
		} finally {
			LOG.application().debug("response : {} ", response_message);
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
		}
	}
}
