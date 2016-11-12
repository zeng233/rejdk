package mytest.java.util.concurrent.locks.ReentrantReadWriteLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;

/**
 * 可重入锁
 * @author zenghua233
 *
 */
public class NestedRWLock {
	private int counter = 0;
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	
	/**
	 * 先读锁后写锁
	 */
	@Test
	public void testDemotion() {
		ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();
		ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();
		
		try {
			readLock.lock();
			//如果读锁没有释放，写锁不能执行
			writeLock.lock();
			writeLock.unlock();
		} finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 可重入性，先写锁，后读锁
	 */
	@Test
	public void testNestedRW() {
		ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();
		ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();
		
		try {
			writeLock.lock();
			readLock.lock();
		} finally {
			readLock.unlock();
			writeLock.unlock();
		}
	}
	
	/**
	 * 设置最大锁数量，内存、CPU飙升，eclipse快死机状态
	 */
	@Test
	public void testMaxRead() {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 65536; i <= 65536; i--) {
			int num = i;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						rwl.readLock().lock();
						System.out.println(num + "-获取到锁");
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						rwl.writeLock().lock();
					}
				}
			});
		}
	}
}
