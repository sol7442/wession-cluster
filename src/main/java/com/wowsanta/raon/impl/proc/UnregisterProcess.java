package com.wowsanta.raon.impl.proc;


import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.UnregisterRequestMessage;
import com.wowsanta.raon.impl.message.UnregisterResonseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;


public class UnregisterProcess extends AbstractSessionProcess {
	public UnregisterProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new UnregisterResonseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			UnregisterRequestMessage request_message = (UnregisterRequestMessage) getRequest().getMessage();
			LOG.application().info("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session != null) {
				WessionCluster.getInstance().delete(session);
			}
			
			UnregisterResonseMessage response_message = (UnregisterResonseMessage) getResponse().getMessage();
			LOG.application().debug("response : {} ", response_message);
		} catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
