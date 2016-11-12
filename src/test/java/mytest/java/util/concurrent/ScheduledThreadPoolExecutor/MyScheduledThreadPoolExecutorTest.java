package mytest.java.util.concurrent.ScheduledThreadPoolExecutor;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class MyScheduledThreadPoolExecutorTest {
	
	/**
	 * 隔2秒再执行
	 * @throws InterruptedException
	 */
	@Test
	public void testSchedule() throws InterruptedException {
		ScheduledThreadPoolExecutor p = new ScheduledThreadPoolExecutor(1);
		CountDownLatch latch = new CountDownLatch(2);
		p.schedule(new Task(latch), 2, TimeUnit.SECONDS);
		p.schedule(new Task(latch), 2, TimeUnit.SECONDS);
		latch.await();
	}
	
	/**
	 * 间隔多少时间执行，不管线程里面执行了多长时间，只要间隔时间到立即执行
	 * 
	 * 如间隔10分钟：
	 * 第一次：start: 00:00 end:00:10
	 * 第二次：start: 02:00 end:02:10
	 * 第三次： start: 04:00 end:04:10
	 * 
	 * 
	 * ScheduledThreadPoolExecutor流程跟踪
	 * @throws Exception
	 */
	@Test
	public void testScheduleAtFixedRate() throws Exception {
		ScheduledThreadPoolExecutor p = new ScheduledThreadPoolExecutor(1);
		CountDownLatch latch = new CountDownLatch(3);
		p.scheduleAtFixedRate(new Task(latch), 0, 1000, TimeUnit.MILLISECONDS);
		latch.await();
	}
	
	/**
	 * 线程完成任务之后的 间隔时间 再执行
	 * 
	 * 如间隔1个小时：
	 * 第一次：start:00:00 end:00:10
	 * 第二次：start:01:10 end:01:20
	 * 第三次：start:02:20 end:02:30
	 * 
	 * 参考quartz实现：如果执行任务超时了会有异常发生
	 * @throws Exception
	 */
	@Test
	public void testScheduleWithFixedDelay() throws Exception {
		ScheduledThreadPoolExecutor p = new ScheduledThreadPoolExecutor(5);
		CountDownLatch latch = new CountDownLatch(3);
		p.scheduleWithFixedDelay(new Task(latch), 0, 1000, TimeUnit.MILLISECONDS);
		latch.await();
	} 
	
	@Test
	public void testExecute() throws InterruptedException {
		ScheduledThreadPoolExecutor p = new ScheduledThreadPoolExecutor(1);
		CountDownLatch latch = new CountDownLatch(1);
		
//		p.schedule(new Task(latch), 1, TimeUnit.SECONDS);
		p.execute(new Task(latch));
		latch.await();
		System.out.println("test Thread");
//		p.shutdown();
	}
	
	class Task implements Runnable {
		CountDownLatch latch;
		
		public Task(CountDownLatch latch) {
			this.latch = latch;
		}
		
		@Override
		public void run() {
			System.out.println("task is executed!" + new Date().getSeconds());
			latch.countDown();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class MyTask implements Runnable {
		@Override
		public void run() {
			System.out.println("This is MyTask!");
		}
	}
}
