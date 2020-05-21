package test.raon.data;

import java.util.Random;

import org.junit.Test;

import com.wowsanta.util.Hex;

public class ByteCharTest {
	@Test
	public void byte_chare_convert_test() {
		//byte[] sample = new byte[2];
		//new Random().nextBytes(sample);
		
		char c1 = (char) (new Random().nextInt(26) + 'a');
		char c2 = (char) (new Random().nextInt(26) + 'a');
		byte[] sample = new byte[2];
		sample[0] = (byte)c1;
		sample[1] = (byte)c2;
		
		
		System.out.println(Hex.toHexString(sample));
		System.out.println(Hex.toHexString(sample[0]));
		System.out.println(Hex.toHexString(sample[1]));
		
		String formatStr = "%c%c";
		String format_str = String.format(formatStr, (char)sample[0], (char)sample[1]);
		
		System.out.println(format_str);
		System.out.println(format_str.getBytes().length);
		System.out.println(Hex.toHexString(format_str.getBytes()));
		System.out.println(Hex.toHexString(format_str.getBytes()[0]));
		System.out.println(Hex.toHexString(format_str.getBytes()[1]));
		
		
		
	}
}
