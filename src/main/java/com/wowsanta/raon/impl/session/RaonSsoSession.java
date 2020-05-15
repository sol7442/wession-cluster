package com.wowsanta.raon.impl.session;

import com.wowsanta.wession.session.Wession;

import lombok.Data;

@Data
public class RaonSsoSession implements Wession  {
	private static final long serialVersionUID = 3174368343310929054L;
	String key;
}
