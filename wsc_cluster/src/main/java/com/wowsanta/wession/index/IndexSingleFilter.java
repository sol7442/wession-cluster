package com.wowsanta.wession.index;

import com.wowsanta.logger.LOG;

import lombok.Data;

@Data
public class IndexSingleFilter {
	String key;
	public IndexFilterOperator operator;
	String value;
	
	public IndexSingleFilter(){}
	public void parse(String filter) {
		try {
			if(filter != null) {
				String[] temp = filter.split(" ");
				if(temp.length != 3) {
					LOG.process().warn("{} IS NOT SINGLE FILTER.", filter);
				}else {
					key   	 = temp[0];
					operator = IndexFilterOperator.valueOf(temp[1].toUpperCase());
					value 	 = temp[2];
				}					
			}

		}catch (Exception e) {
			LOG.process().warn("{} SINGLE FILTER ERROR : {}", filter, e.getMessage());
		}
	}
	
}
