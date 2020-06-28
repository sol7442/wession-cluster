package com.wowsanta.raon.impl.proc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RES_ACT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
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

public class AccountListProcess extends AbstractSessionProcess {
	public AccountListProcess() {
		setRequest(new SessionRequest(new AccountListRequestMessage()));
	}

	public AccountListProcess(RaonSessionMessage request) {
		setRequest(new SessionRequest(request));
	}

	@Override
	public void porcess() throws ServerException {

		AccountListRequestMessage request_message  = (AccountListRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		try {
			LOG.application().debug("request  : {} ", request_message);
			SearchRequestMessage search_request = new SearchRequestMessage();
			
			String filter = request_message.getFilter().getValue();
			
			search_request.setFilter("userId sw " + filter);
			search_request.setStartIndex(0);
			search_request.setCount(0);
			search_request.setOrderKey("modifyTime");
			
			@SuppressWarnings("unchecked")
			SearchResponseMessage<RaonSession> search_response =  WessionCluster.getInstance().search(search_request);
			
			List<RaonSession> resource_list = search_response.getResources();
			
			
			Map<String, List<RaonSession>> accout_session_map = new HashMap<String, List<RaonSession>>();
			for (RaonSession session : resource_list) {
				String user_id = session.getUserId();
				List<RaonSession> user_session_list = accout_session_map.get(user_id);
				if(user_session_list == null) {
					user_session_list = new ArrayList<RaonSession>();
					accout_session_map.put(user_id, user_session_list);
				}
				user_session_list.add(session);
			}

			
			AccountListResonseMessage account_list_response = new AccountListResonseMessage();
			SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			
			Object[] account_list = accout_session_map.keySet().toArray();
			

			int page = request_message.getPage().getValue();
			int rows = request_message.getRows().getValue();
			
			int _t_size = account_list.length;
			int _f_index = page*rows;
			int _t_index;
			if(_t_size >= (page+1)*rows) {
				_t_index = (page+1)*rows;
			}else {
				_t_index = _t_size;
			}
			

			int _count = 0;
			for(int i=_f_index;i<_t_index;i++ ) {
				String user_id = (String) account_list[i];
				
				List<RaonSession> user_session_list = accout_session_map.get(user_id);
				Date create_time = null;
				Date access_time = null;
				for (RaonSession session : user_session_list) {
					create_time = compare(create_time, session.getCreateTime(), true);
					access_time = compare(access_time, session.getModifyTime(), false);
				}
				RES_ACT resource_account = new RES_ACT();
				resource_account.setUserId(new STR(user_id));
				resource_account.setSessionCount(new INT(user_session_list.size()));
				resource_account.setAccessTime(new STR(date_format.format(create_time)));
				resource_account.setCreateTime(new STR(date_format.format(access_time)));
				
				account_list_response.addResourceAcount(resource_account);
				_count++;
			}
			
			account_list_response.setTotalCount(new INT(search_response.getTotalResults()));
			account_list_response.setCount1(new INT(_count));
			account_list_response.setCount2(new INT(_count));
			
			
			response_message = account_list_response;
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
