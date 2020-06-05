/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package com.wowsanta.wession.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wowsanta.wession.session.Wession;

public enum Operator
{
    EQ("eq", "equal" , "="){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			return (List<Wession>) map.get(filter);
		}
    },
    CO("co", "contains", "like" ){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    SW("sw", "starts with", "like"){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			List<Wession> list = new ArrayList<Wession>();
			Set<String> key_set = map.keySet();
			for (String key_value : key_set) {
				if(key_value.startsWith(filter)) {
					list.addAll(map.get(key_value));
				}
			}
			return list;
		}
    },
    PR("pr", "present (has value)", "is not null"){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    NP("np", "not present (has not value)", "is null"){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    GT("gt", "greater than" , ">"){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    GE("ge", "greater than or equal" , ">="){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    LT("lt", "less than", "<"){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    LE("le", "less than or equal", "<="){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    AND("and", "logical and", "and"){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    OR("or", "logical or", "or"){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			// TODO Auto-generated method stub
			return null;
		}
    },
    UNKNOWN("", "unknown operator", ""){
		@Override
		public List<Wession> filter(Map<String,List<Wession>> map, String filter) {
			return null;
		}
    };
    
    private String val;
    private String rdb;
    private String desc;
    
    private Operator( String val, String desc , String rdb)
    {
        this.val  = val;
        this.desc = desc;
        this.rdb  = rdb;  
    }
    public abstract List<Wession> filter(final Map<String,List<Wession>> map , String filter);
    
    public String getValue() {
    	return val;
    }
    public String getDescription() {
    	return desc;
    }
    public String getRdbOperator() {
		return this.rdb;
	}
    
    public static Operator getByName( String name )
    {
        name = name.toLowerCase();
        switch(name) {
        case "EQ" : return EQ;
        case "CO" : return CO;
        case "SW" : return SW;
        case "PR" : return PR;
        case "NP" : return NP;
        case "GT" : return GT;
        case "LT" : return LT;
        case "LE" : return LE;
        case "AND": return AND;
        case "OR" : return OR;
        default   : return UNKNOWN;
        }
    }
	

}
