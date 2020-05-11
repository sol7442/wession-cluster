package test.raon.data;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.wowsanta.util.Hex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufferTest {

	@Test
	public void buffer_controll_test() {
		ByteBuffer buffer = ByteBuffer.allocate(32);
		log.debug("alloc-32 : {} ", buffer);
		
		buffer.put("12345678".getBytes());
		log.debug("put-8 : {} ", buffer);
		
		buffer.flip();
		log.debug("flip : {} ", buffer);
		
		byte[] data = new byte[2];
		buffer.get(data);
		log.debug("read-2 : {},{} -> {} - {}",buffer,buffer.remaining(),data,new String(data));
		
		buffer.get(data);
		log.debug("read-2 : {},{} -> {} - {}",buffer,buffer.remaining(),data,new String(data));
		
		buffer.compact();
		log.debug("compact : {} - {}", buffer,buffer.remaining());

		buffer.put("12345678".getBytes());
		log.debug("put-8 : {} ", buffer);
		
		buffer.flip();
		log.debug("flip : {} ", buffer);
		
		buffer.mark();
		log.debug("mark : {} ", buffer);
		
		buffer.get(data);
		log.debug("read-2 : {},{} -> {} - {}",buffer,buffer.remaining(),data,new String(data));
		
		buffer.get(data);
		log.debug("read-2 : {},{} -> {} - {}",buffer,buffer.remaining(),data,new String(data));
		
		buffer.reset();
		log.debug("reset : {} -> {}", buffer, buffer.remaining());
	
		buffer.compact();
		log.debug("compact : {} - {}", buffer,buffer.remaining());
		
		buffer.put("1234567811".getBytes());
		log.debug("put-10 : {} ", buffer);
		
		
		buffer.flip();
		log.debug("flip : {} ", buffer);
		
		buffer.get(data);
		log.debug("read-2 : {},{} -> {} - {}",buffer,buffer.remaining(),data,new String(data));
		
		int count = 0 ;
		while(count < 9 ) {
			buffer.get(data);
			log.debug("read-2 : {},{} -> {} - {}",buffer,buffer.remaining(),data,new String(data));
			count++;
		}
		
		buffer.compact();
		log.debug("compact : {} ", buffer);
		
	}
}
