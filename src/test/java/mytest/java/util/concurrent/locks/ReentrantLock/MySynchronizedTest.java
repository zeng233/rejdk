package mytest.java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * synchroniezd其他线程不会阻塞，而Lock会阻塞
 * @author zenghua233
 *
 */
public class MySynchronizedTest {
	public CountDownLatch latch = new CountDownLatch(3);
	
	@Test
	public void testAdd() throws Exception {
		MySynchronizedTest t = new MySynchronizedTest();
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					t.add();
				}
			});
		}
		
		latch.await();
		executor.shutdown();
	}
	
	public synchronized void add() {
		String name = Thread.currentThread().getName();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		latch.countDown();
		System.out.println("执行线程名称：" + name);
	}
}
