package com.wowsanta.wession.impl.server;


import java.io.IOException;
import java.util.Date;

import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.proc.AbstractSessionProcess;
import com.wowsanta.raon.impl.proc.SessionResponse;
import com.wowsanta.server.Message;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.server.ServiceProcess;
import com.wowsanta.server.nio.NioConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RaonSessionServiceDispatcher extends ServiceDispatcher{

	long start_time;
	long end_time;
	
	@Override
	public void before(ServiceProcess<?, ?> process) {
		try {
			Date now = new Date();
			log.debug("before : {} / {}", process.getClass().getName(), now);
			
			start_time = now.getTime();
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		} 
	}

	@Override
	public void dispatcher(ServiceProcess<?,?> process) {
		try {
			process.porcess();
		}catch (Exception e) {
			errer(process,e);
		}finally {
			try {
				process.getResponse().getMessage().flush();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private void errer(ServiceProcess<?, ?> process, Exception error) {
		try {
			log.error(error.getMessage(), error);
			
			AbstractSessionProcess session_process = (AbstractSessionProcess) process;
			RaonSessionMessage request_message = (RaonSessionMessage) session_process.getRequest().getMessage();
			
			ErrorResonseMessage error_message = new ErrorResonseMessage();
			error_message.setRequest(request_message.getCommand());
			error_message.setCode(new INT(5000));
			error_message.setMessage(new STR("SERVER ERROR : " + error.getMessage()));
			
			session_process.setResponse(new SessionResponse(error_message));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void after(ServiceProcess<?,?> process) {
		try {
			Message message = process.getResponse().getMessage();
			NioConnection connection = (NioConnection) process.getConnection();
			connection.write(message.toBytes());
			connection.write0();
			end_time = System.currentTimeMillis();
			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			log.debug("after : {} / {} ", process.getClass().getName(), end_time - start_time);
		}
	}
}
