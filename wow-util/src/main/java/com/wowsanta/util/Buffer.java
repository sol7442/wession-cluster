package com.wowsanta.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class Buffer {
	public static ByteBuffer toBuffer(Serializable obj)throws IOException {
		return ByteBuffer.wrap(toByteArray(obj));
	}
	
    public static byte[] toByteArray(Serializable obj) throws IOException {
    	byte[] bytes = null;
    	try (
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos)
		){
    		oos.writeObject(obj);
    		oos.flush();
    		
            bytes = bos.toByteArray();
    	}
		
		return bytes;
    }


	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T toObject(byte[] bytes, Class<T> type)throws IOException, ClassNotFoundException {
		T object = null;
		try(
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
		){
			object = (T) ois.readObject();
		}
		
		return object;
	}

}
