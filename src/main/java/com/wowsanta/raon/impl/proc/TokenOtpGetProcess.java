package com.wowsanta.raon.impl.proc;


import java.util.Random;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetRequestMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetResponseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.server.ServerException;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.WessionCluster;

import lombok.extern.slf4j.Slf4j;

public class TokenOtpGetProcess extends AbstractSessionProcess {
	public TokenOtpGetProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new TokenOtpGetResponseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			TokenOtpGetRequestMessage  request_message  = (TokenOtpGetRequestMessage) getRequest().getMessage();
			LOG.process().info("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session == null) {
				throw new ServerException("Session Not Found : " + session_key);
			}
			
			String opt_crt = Hex.toHexString(new Random().nextInt());
			String opt_val = Hex.toHexString(new Random().nextInt());

			session.setAttribute("opt.val", opt_val);
			session.setAttribute("opt.crt", opt_crt);
			
			TokenOtpGetResponseMessage response_message = (TokenOtpGetResponseMessage) getResponse().getMessage();
			
			RSTRS data = new RSTRS();
			data.add((byte)0x1,opt_val);
			data.add((byte)0x2,opt_crt);
			
			response_message.setData(data);
			
			LOG.process().info("response : {} ", response_message);
		} catch (Exception e) {
			LOG.process().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
