package com.wowsanta.raon.impl.proc;


import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.UserDataDelRequestMessage;
import com.wowsanta.raon.impl.message.UserDataDelResponseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;


public class UserDataDelProcess extends AbstractSessionProcess {
	public UserDataDelProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UserDataDelResponseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			UserDataDelRequestMessage  request_message  = (UserDataDelRequestMessage) getRequest().getMessage();
			LOG.application().info("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session != null) {
				String user_data = (String) session.getAttribute("user.data");
				LOG.application().debug("user.data : {} ", user_data);
				session.removeAttribute("user.data");
				WessionCluster.getInstance().update(session);				
			}
			
			UserDataDelResponseMessage response_message = (UserDataDelResponseMessage) getResponse().getMessage();
			response_message.setLot(new INT((int)System.currentTimeMillis()));
			
			LOG.application().info("response : {} ", response_message);
		} catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
