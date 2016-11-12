package mytest.java.util.concurrent.locks.ReentrantLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class BasicReentrantLockTest {
	public List<Long> list = new ArrayList<>();
	public Lock lock = new ReentrantLock();

	/**
	 * 局部变量的lock可重复读
	 * @param taskName
	 */
	public void insert(String taskName) {
		//局部变量的lock会有一个副本
		Lock lock = new ReentrantLock(); // 注意这个地方
		lock.lock();
		try {
			lock.lock();
			System.out.println("thread-" + taskName + "-得到了锁");
			list.add(Thread.currentThread().getId());
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.out.println("thread-" + taskName + "释放了锁");
			lock.unlock();
			lock.unlock();
		}
	}
	
	/**
	 * 全局变量Lock
	 * @param taskName
	 */
	public void insertByTryLock(String taskName) {
		Lock lock = this.lock;
		if (lock.tryLock()) {
			try {
				System.out.println("thread-" + taskName + "-得到了锁");
				list.add(Thread.currentThread().getId());
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				System.out.println("thread-" + taskName + "释放了锁");
				lock.unlock();
			}
		} else {
			System.out.println("thread-" + taskName + "获取锁失败");
		}
	}

	@Test
	public void testLocalLock() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			executor.execute(new TaskA(String.valueOf(i)));
		}
	}

	
	
	
	class TaskA implements Runnable {
		public String taskName;

		public TaskA(String taskName) {
			this.taskName = taskName;
		}

		@Override
		public void run() {
//			insert(taskName);
			insertByTryLock(taskName);
		}

	}
}
