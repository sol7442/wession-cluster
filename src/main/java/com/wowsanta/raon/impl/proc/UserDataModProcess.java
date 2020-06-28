package com.wowsanta.raon.impl.proc;


import java.util.Date;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.UserDataModRequestMessage;
import com.wowsanta.raon.impl.message.UserDataModResponseMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.repository.RespositoryException;


public class UserDataModProcess extends AbstractSessionProcess {
	public UserDataModProcess() {
		setRequest(new SessionRequest(new UserDataModRequestMessage()));
		
	}

	public UserDataModProcess(RaonSessionMessage request) {
		setRequest(new SessionRequest(request));
	}

	@Override
	public void porcess() throws ServerException {
		UserDataModRequestMessage  request_message  = (UserDataModRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		try {
			LOG.application().debug("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			BYTE4 index         = request_message.getSessionIndex();
			String user_data    = request_message.getData().getValue();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session != null) {
				session.setModifyTime(new Date());
				session.setAttribute("user.data", user_data);
				
				WessionCluster.getInstance().update(session);
				
				UserDataModResponseMessage userdata_mod_message = new UserDataModResponseMessage();
				userdata_mod_message.setLot(new INT(session.getCreateTime().getTime()));
				
				response_message = userdata_mod_message;
			}else {
				ErrorResonseMessage erro_messge = new ErrorResonseMessage();
				erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_ADDUSERDATA.getValue()));
				erro_messge.setCode(new INT(RaonError.ERRSESSIONISNOTEXIST.getCode()));
				erro_messge.setMessage(new STR(RaonError.ERRSESSIONISNOTEXIST.getMessage()));
				
				response_message = erro_messge;;
			}
		} catch (RespositoryException e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_ADDUSERDATA.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		}catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		} finally {
			LOG.application().debug("response : {} ", response_message);
			
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
		}
	}

}
