package com.wowsanta.raon.impl.proc;


import java.util.Date;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.UserDataAddRequestMessage;
import com.wowsanta.raon.impl.message.UserDataAddResponseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;


public class UserDataAddProcess extends AbstractSessionProcess {
	public UserDataAddProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UserDataAddResponseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			UserDataAddRequestMessage  request_message  = (UserDataAddRequestMessage) getRequest().getMessage();
			LOG.application().info("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			String user_data    = request_message.getData().getValue();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session != null) {
				session.setModifyTime(new Date());
				session.setAttribute("user.data", user_data);
				
				WessionCluster.getInstance().update(session);				
			}
			
			UserDataAddResponseMessage response_message = (UserDataAddResponseMessage) getResponse().getMessage();
			response_message.setLot(new INT((int)System.currentTimeMillis()));
			
			LOG.application().info("response : {} ", response_message);
		} catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
