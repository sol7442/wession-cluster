package com.wowsanta.util.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

public class JsonConfiguration {
	public static transient GsonBuilder builder = new GsonBuilder();
	static {
		builder.registerTypeAdapter(JsonConfiguration.class, new ConfigurationTypAdapter());
		builder.setPrettyPrinting();
		builder.disableHtmlEscaping();
	}
	
	public static void addTypeAdapter(Class<?> clazz) {
		builder.registerTypeAdapter(clazz, new ConfigurationTypAdapter());
	}
	public String toString(boolean pretty) {
		Gson gson = builder.create();
		return gson.toJson(this);
	}
	public void save(String file_name) {
		try (FileWriter writer = new FileWriter(file_name)) {
			Gson gson = builder.create();
			writer.write(gson.toJson(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static <T extends JsonConfiguration > T load(String file_name, Class<T> type) {
		T config = null;
		File file = new File(file_name);
		if(file.exists() && file.isFile()) {
			Gson gson = builder.create();
			try (JsonReader reader = new JsonReader(new FileReader(file))) {
				return gson.fromJson(reader, type);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.err.println("---");
		}
		return config;
	}
	public static <T> TypeAdapter<T> getAdapter(Class<T> type){
		return builder.create().getAdapter(type);
	}
}
