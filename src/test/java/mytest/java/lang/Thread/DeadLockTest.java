package mytest.java.lang.Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类似于银行转账，要分别给X,Y账户加锁
 * X->Y,
 * Y->X
 * @author zenghua233
 *
 */
public class DeadLockTest {
	private Object left = new Object();
	private Object right = new Object();
	
	public static void main(String[] args) {
		DeadLockTest t = new DeadLockTest();
		t.execute();
	}
	
	public void execute() {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					leftRight();
				}
			});
		}
		
		for (int j = 0; j < 50; j++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					rightLeft();
				}
			});
		}
		
		executor.shutdown();
	}
	
	/**
	 * 
	 */
	public void leftRight() {
		int i = 0;
		synchronized (left) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (right) {
				String name = Thread.currentThread().getName();
				i++;
				System.out.println(name + " : " + i);
			}
		}
	}
	
	public void rightLeft() {
		int j = 10;
		synchronized (right) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (left) {
				String name = Thread.currentThread().getName();
				j++;
				System.out.println(name + " : " + j);
			}
		}
	}
}
