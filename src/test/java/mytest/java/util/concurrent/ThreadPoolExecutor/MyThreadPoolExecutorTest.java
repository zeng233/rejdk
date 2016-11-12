package mytest.java.util.concurrent.ThreadPoolExecutor;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class MyThreadPoolExecutorTest {
	
	/**
	 * 一个线程有异常，其他线程不会受到改线程的异常信息影响，会继续执行
	 */
	@Test
	public void testRun() throws Exception {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		executor.execute(new Task());
		for (int i = 0; i < 5; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(new Date().getTime());
				}
			});
		}
		
		Thread.sleep(3000);
		System.out.println("线程池存在的线程：" + executor.getActiveCount());
		System.out.println("完成任务的线程：" + executor.getCompletedTaskCount());
	}
	
	class Task implements Runnable {
		@Override
		public void run() {
			throw new RuntimeException();
//			try {
//				int i = 1/0;
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
		}
	}
}
