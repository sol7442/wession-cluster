package com.wowsanta.raon.impl.proc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RES_ACT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.AccountDelRequestMessage;
import com.wowsanta.raon.impl.message.AccountDelResonseMessage;
import com.wowsanta.raon.impl.message.AccountListRequestMessage;
import com.wowsanta.raon.impl.message.AccountListResonseMessage;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;

public class AccountDelProcess extends AbstractSessionProcess {
	public AccountDelProcess() {
		setRequest(new SessionRequest(new AccountDelRequestMessage()));
	}

	public AccountDelProcess(RaonSessionMessage request) {
		setRequest(new SessionRequest(request));
	}

	@Override
	public void porcess() throws ServerException {

		AccountDelRequestMessage request_message  = (AccountDelRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		try {
			LOG.application().debug("request  : {} ", request_message);
			
			STR[] accounts_array = request_message.getAccounts();
			for(int i= 0; i<accounts_array.length; i++) {
				
				SearchRequestMessage search_request = new SearchRequestMessage();
				
				String filter = "userId eq " + accounts_array[i].getValue();
				search_request.setFilter(filter);
				search_request.setStartIndex(0);
				search_request.setCount(0);
				search_request.setOrderKey("modifyTime");
				
				@SuppressWarnings("unchecked")
				SearchResponseMessage<RaonSession> search_response =  WessionCluster.getInstance().search(search_request);
				
				List<RaonSession> resource_list = search_response.getResources();
				for (RaonSession session : resource_list) {
					WessionCluster.getInstance().delete(session);					
				}
			}
			
			response_message = new AccountDelResonseMessage();
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
