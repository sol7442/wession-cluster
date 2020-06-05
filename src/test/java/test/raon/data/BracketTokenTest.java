package test.raon.data;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class BracketTokenTest {

	final String str = "userId sw test";//"((userId sw test)or(userId sw dev))and(modifyDate eq 1234567)";
	
	@Test
	public void tokenize_test() {
		StringTokenizer st = new StringTokenizer(str,"");
		System.out.println(st.countTokens());
		while(st.hasMoreTokens()) {
			System.out.println("1 : " + st.nextToken());
		}
		System.out.println("=========================================");
		String[] tm = str.split(" ");
		for (String sp : tm) {
			System.out.println("1 : " + sp);
		}
		
		System.out.println("=========================================");
		Pattern pattern = Pattern.compile("(sw|eq)");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()) {
			System.out.println("1- : " + matcher.group());
		}
	}
}
