package com.wowsanta.raon.impl.proc;

import java.util.Date;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.SessionDelRequestMessage;
import com.wowsanta.raon.impl.message.SessionDelResonseMessage;
import com.wowsanta.raon.impl.message.UnregisterResonseMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.repository.RespositoryException;

public class SessionDelProcess extends AbstractSessionProcess {
	public SessionDelProcess() {
		setRequest(new SessionRequest(new SessionDelRequestMessage()));
	}

	public SessionDelProcess(RaonSessionMessage request) {
		setRequest(new SessionRequest(request));
	}

	@Override
	public void porcess() throws ServerException {

		SessionDelRequestMessage request_message  = (SessionDelRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		try {
			LOG.application().debug("request  : {} ", request_message);
			
			String user_id = request_message.getUserId().getValue();
			byte[] index   = request_message.getIndex().getValue();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index);
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session != null) {
				WessionCluster.getInstance().delete(session);
				response_message = new UnregisterResonseMessage();
			}else {
				ErrorResonseMessage erro_messge = new ErrorResonseMessage();
				erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_DELSESSION.getValue()));
				erro_messge.setCode(new INT(RaonError.ERRSESSIONISNOTEXIST.getCode()));
				erro_messge.setMessage(new STR(RaonError.ERRSESSIONISNOTEXIST.getMessage()));
				
				response_message = erro_messge;;
			}
			
			response_message = new SessionDelResonseMessage();
		} catch (RespositoryException e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		} catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		}
		finally {
			LOG.application().debug("response : {} ", response_message);
			
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
			
		}
	}

	private Date compare(Date data1, Date data2, boolean late) {
		if(data1 == null) return data2;
		
		return (data1.getTime() < data2.getTime())&&late ? data1 : data2;
	}

}
