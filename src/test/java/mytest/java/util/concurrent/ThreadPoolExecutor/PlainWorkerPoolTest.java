package mytest.java.util.concurrent.ThreadPoolExecutor;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

public class PlainWorkerPoolTest {

	@Test
	public void testRun() throws Exception {
		PlainWorkerPool pool = new PlainWorkerPool(new LinkedBlockingQueue<Runnable>(), 10);
		for (int i = 0; i < 10; i++) {
			pool.execute(new Task());
		}
		
		Thread.sleep(3000);
	}
	
	class Task implements Runnable {
		
		
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String name = Thread.currentThread().getName();
			System.out.println(name);
		}
	}
}
