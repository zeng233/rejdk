package mytest.java.util.concurrent.locks.ReentrantReadWriteLock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;

/**
 * ReentrantReadWriteLock默认为非公平锁（NonfairSync）
 * 参考：https://www.zhihu.com/question/36964449/answer/71678967
 * 
 * 公平锁（先来先得）
 * 非公平锁（不考虑排队）
 * @author zenghua233
 *
 */
public class ReentrantReadWriteLockTest {
	private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	private ReentrantLock relock = new ReentrantLock();
	
	//同步块读，会把整个方法锁定
	public synchronized void read(String taskName) {
		Long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start <= 1) {
			System.out.println(taskName + "-正在读操作");
		}
		System.out.println("==========" + taskName + "已经读完");
	}
	
	/**
	 * 一直要等到一个线程读完了，另外一个线程才可以读
	 * @param taskName
	 */
	public void readByRelock(String taskName) {
		relock.lock();
		
		try {
			Long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start <= 1) {
				System.out.println(taskName + "-正在读操作");
			}
			System.out.println("==========" + taskName + "已经读完");
		} finally {
			relock.unlock();
		}
	}
	
	/**
	 * 可以实现多个线程交叉读取
	 * @param taskName
	 */
	public void readByLock(String taskName) {
		rwlock.readLock().lock();
		
		try {
			Long start = System.currentTimeMillis();
			while (System.currentTimeMillis() - start <= 1) {
				System.out.println(taskName + "-正在读操作");
			}
			System.out.println("==========" + taskName + "已经读完");
		} finally {
			rwlock.readLock().unlock();
		}
	}
	
	/**
	 * 
	 * @param taskName
	 */
	public void writeByLock(String taskName) {
		while (true) {
			try {
				rwlock.writeLock().lock();
			} finally {
				rwlock.writeLock().unlock();
			}
			
		}
		
	}
	
	/**
	 * rwlock可以交叉读写
	 * @throws Exception
	 */
	@Test
	public void testRead() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(3);
		for (int i = 0; i < 2; i++) {
			executor.execute(new Task("thread" + i));
			latch.countDown();
		}
		
//		new Thread(new Task("a")).start();
//		new Thread(new Task("b")).start();
		latch.await(2, TimeUnit.SECONDS);
	}
	
	class Task implements Runnable {
		private String taskName;
		
		public Task(String taskName) {
			this.taskName = taskName;
		}
		
		public void run() {
//			read(taskName);
			readByLock(taskName);
//			readByRelock(taskName);
		}
	}
}
