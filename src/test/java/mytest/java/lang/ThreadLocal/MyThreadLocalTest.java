package mytest.java.lang.ThreadLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class MyThreadLocalTest {
	ThreadLocal<Object> local = new ThreadLocal<>();

	/**
	 * 测试是否内存溢出，用jconsole等工具查看内存是否一直增加
	 * 
	 * ajava.lang.ThreadLocal.MemoryLeak已经修复内存溢出
	 */
	@Test
	public void testOOM() {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (long i = 0; i < 999999999; i++) {
			executor.execute(new Task());
		}
		System.out.println("执行完毕");
		
//		for (long i = 0; i < 999999999; i++) {
//			local.set(new Object());
//		}
		
//		while(true) {
//			local.set(new Object());
//		}
	}
	
	@Test
	public void testOOMByList() {
		List<Object> list = new ArrayList<Object>();
		while(true) {
			list.add(new Object());
		}
	}
	
	class Task implements Runnable {
		@Override
		public void run() {
			local.set(new Object());
		}
	}
	
}
