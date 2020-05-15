package com.wowsanta.raon.impl.proc;

import java.util.Date;
import java.util.Random;

import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.RegisterRequestMessage;
import com.wowsanta.raon.impl.message.RegisterResonseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.server.ServerException;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.manager.ClusterManager;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterProcess extends AbstractSessionProcess {
	public RegisterProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new RegisterResonseMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			
			
			RegisterRequestMessage request_message = (RegisterRequestMessage) getRequest().getMessage();
			log.debug("request : {} ", request_message);

			byte[] token_key = new byte[8];
			byte[] random_key = new byte[8];
			new Random().nextBytes(token_key);
			new Random().nextBytes(random_key);

			String user_id = request_message.getUserId().getValue();
			String session_key = Hex.toHexString(random_key);
			String token_value = Hex.toHexString(token_key);
			Date   now_time    = new Date();

			
			SearchRequestMessage  request  = new SearchRequestMessage();
			request.setFilter("userId="+user_id);
			SearchResponseMessage response = WessionCluster.getInstance().search(request);
			
			log.debug("search request  : {} ", request);
			log.debug("search response : {} ", response);
			
			
			RSTRS data = new RSTRS();
			data.add((byte) 0x00,session_key);
			data.add((byte) 0x01,token_value);

			
			RaonSession session = new RaonSession();
			session.setKey(session_key);
			session.setUserId(user_id);
			session.setToken(token_value);
			session.setCreateTime(now_time);
			session.setModifyTime(now_time);
			
			WessionCluster.getInstance().create(session);
			
			
			RegisterResonseMessage response_message = (RegisterResonseMessage) getResponse().getMessage();

			
			
			response_message.setLot(new INT((int) System.currentTimeMillis()));
			
			BYTE4 index = new BYTE4();
			index.set(1,(byte) 'A');
			index.set(3,(byte)'"');
			response_message.setSessionIndex(index);			
			response_message.setData(data);;
			
			log.debug("request : {} ", response_message);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
