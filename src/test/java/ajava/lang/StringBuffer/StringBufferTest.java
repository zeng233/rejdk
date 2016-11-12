package ajava.lang.StringBuffer;

public class StringBufferTest {
	public static void main(String[] args) {
//		StringBuffer sb = new StringBuffer("123");
//		System.out.println(sb.reverse());
		
		StringBufferTest t = new StringBufferTest();
		t.testReverse("1234");
	}
	
	public void testReverse(String str) {
		char[] chars = str.toCharArray();
		int n = chars.length - 1;
		for (int i = (n) >> 1; i >= 0 ; i--) {
			char temp1 = chars[i];
			char temp2= chars[n - i];
			
			chars[i] = temp2;
			chars[n - i] = temp1;
		}
		System.out.println(new String(chars));
	}
}
