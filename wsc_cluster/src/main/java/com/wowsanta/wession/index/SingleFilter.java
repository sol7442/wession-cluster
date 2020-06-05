package com.wowsanta.wession.index;

import com.wowsanta.logger.LOG;

import lombok.Data;

@Data
public class SingleFilter {
	String key;
	Operator op;
	String value;
	
	public SingleFilter(){}
	public void parse(String filter) {
		try {
			String[] temp = filter.split(" ");
			if(temp.length != 3) {
				LOG.process().warn("{} IS NOT SINGLE FILTER.", filter);
			}else {
				key   = temp[0];
				op    = Operator.valueOf(temp[1].toUpperCase());
				value = temp[2];
			}	
		}catch (Exception e) {
			LOG.process().warn("{} SINGLE FILTER ERROR : {}", filter, e.getMessage());
		}
	}
	
}
