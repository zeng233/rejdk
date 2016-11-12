package mytest.java.lang.Byte;

import org.junit.Test;

public class ByteTest {
	
	/**
	 * 参考：
	 * http://jdk.io/jdk-io-2014/program/core-java/14-jvm-bytecode-engineering
	 */
	@Test
	public void testRange() {
		byte a = 127;
		byte b = 34;
		//为什么必须强转，数字默认为int类型？TODO
		byte c = (byte) (a + b);
		System.out.println(c);
	}
}
