package com.wowsanta.raon.impl.proc;


import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.UserDataGetRequestMessage;
import com.wowsanta.raon.impl.message.UserDataGetResponseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;

public class UserDataGetProcess extends AbstractSessionProcess {
	public UserDataGetProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UserDataGetResponseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			UserDataGetRequestMessage  request_message  = (UserDataGetRequestMessage) getRequest().getMessage();
			LOG.application().info("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session == null) {
				throw new ServerException("Session Not Found : " + session_key);
			}

			LOG.application().debug("session : {}", session);
			String user_data = (String)session.getAttribute("user.data");
			if(user_data != null) {
				UserDataGetResponseMessage response_message = (UserDataGetResponseMessage) getResponse().getMessage();
				response_message.setLot(new INT((int)System.currentTimeMillis()));
				response_message.setData(new STR(user_data));
			}else {
				ErrorResonseMessage error_message = new ErrorResonseMessage();
				error_message.setRequest(request_message.getCommand());
				error_message.setCode(new INT(5000));
				error_message.setMessage(new STR("User Data is Null"));
				
				setResponse(new SessionResponse(error_message, getRequest().getSession()));
			}
			

			
			LOG.application().info("response : {} ", getResponse().getMessage());
		} catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
