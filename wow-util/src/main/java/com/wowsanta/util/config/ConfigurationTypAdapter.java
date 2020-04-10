package com.wowsanta.util.config;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ConfigurationTypAdapter extends TypeAdapter<JsonConfiguration> {

	private static final String NAME_CLASS = "confClass";
	private static final String NAME_CONIF = "confValue";;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void write(JsonWriter out, JsonConfiguration config) throws IOException {
		if(config != null) {
			TypeAdapter adpater = JsonConfiguration.getAdapter(config.getClass());

			out.beginObject();
			
			out.name(NAME_CLASS);
			out.value(config.getClass().getName());
			
			out.name(NAME_CONIF);
			adpater.write(out, config);
			
			out.endObject();
		}
	}

	@Override
	public JsonConfiguration read(JsonReader in) throws IOException {
		JsonConfiguration config = null;
		in.beginObject();
        while ( in.hasNext() ) {
    		TypeAdapter<?> adpater = null;
        	JsonToken token = in.peek();
        	if(token.equals(JsonToken.NAME)) {
        		if(NAME_CLASS.equals(in.nextName())){
        			try {
						adpater = JsonConfiguration.getAdapter(Class.forName(in.nextString()));
					} catch (ClassNotFoundException e) {
						throw new IOException(e.getMessage());
					}
        		}
        		if(NAME_CONIF.equals(in.nextName())){
        			config = (JsonConfiguration) adpater.read(in);
        		}
        	}
        }
        in.endObject();
		return config;
	}

}
