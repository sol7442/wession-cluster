package com.wowsanta.raon.impl.proc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RES_SES;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.SessionListRequestMessage;
import com.wowsanta.raon.impl.message.SessionListResonseMessage;
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

public class SessionListProcess extends AbstractSessionProcess {
	public SessionListProcess() {
		setRequest(new SessionRequest(new SessionListRequestMessage()));
	}

	@Override
	public void porcess() throws ServerException {

		SessionListRequestMessage request_message  = (SessionListRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		try {
			LOG.application().info("request  : {} ", request_message);
			SearchRequestMessage search_request = new SearchRequestMessage();
			String user_id = request_message.getUserId().getValue();
			
			String filter = "userId eq " + user_id;
			search_request.setFilter(filter);
			search_request.setStartIndex(0);
			search_request.setCount(0);
			search_request.setOrderKey("modifyTime");
			
			@SuppressWarnings("unchecked")
			SearchResponseMessage<RaonSession> search_response =  WessionCluster.getInstance().search(search_request);
			List<RaonSession> resource_list = search_response.getResources();

			

			SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			List<RES_SES> session_list = new ArrayList<RES_SES>();
			for (RaonSession session : resource_list) {
				RES_SES resource_sesssion = new RES_SES();
				resource_sesssion.setIndex(new BYTE4(session.getIndex()));
				resource_sesssion.setCreateTime(new STR(date_format.format(session.getCreateTime())));
				resource_sesssion.setAccessTime(new STR(date_format.format(session.getModifyTime())));
				
				session_list.add(resource_sesssion);
			};

			SessionListResonseMessage session_list_response = new SessionListResonseMessage();
			session_list_response.setCount(new INT(resource_list.size()));
			session_list_response.setResources(session_list);
			
			
			response_message = session_list_response;
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
			LOG.application().info("response : {} ", response_message);
			
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
			
		}
	}
}
