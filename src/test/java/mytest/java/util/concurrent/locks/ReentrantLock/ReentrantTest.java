package mytest.java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试可重入性
 * 
 * @author zenghua233
 *
 */
public class ReentrantTest {
	private ReentrantLock lock = new ReentrantLock();
	
	public static void main(String[] args) {
		ReentrantTest t = new ReentrantTest();
		t.enter();
	}
	
	public void enter() {
		try {
			lock.lock();
			this.entrant();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 嵌套内部锁的状态会递增
	 */
	public void entrant() {
		try {
			lock.lock();
		} finally {
			lock.unlock();
		}
	}
}
