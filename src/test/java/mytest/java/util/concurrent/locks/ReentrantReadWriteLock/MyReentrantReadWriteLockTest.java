package mytest.java.util.concurrent.locks.ReentrantReadWriteLock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyReentrantReadWriteLockTest {
	static final MyReentrantReadWriteLockTest instance = new MyReentrantReadWriteLockTest();
	int counter = 0;
	ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);

	public void read() {
		while (true) {
			rwl.readLock().lock();
			try {
				System.out.println("Counter is " + counter);
			} finally {
				rwl.readLock().unlock();
			}
			try {
				Thread.currentThread().sleep(1000);
			} catch (Exception ie) {
			}
		}
	}

	/**
	 * 如果同时执行读写锁，要等到另外一个释放完了，才能获取锁
	 */
	public void write() {
		while (true) {
			rwl.writeLock().lock();
			try {
				counter++;
				System.out.println("Incrementing counter.  Counter is " + counter);
			} finally {
				rwl.writeLock().unlock();
			}
			try {
				Thread.currentThread().sleep(3000);
				if (counter == 3) break;
			} catch (Exception ie) {
			}
		}
	}

	public static void main(String[] args) {
		instance.write();
		instance.read();
	}
}