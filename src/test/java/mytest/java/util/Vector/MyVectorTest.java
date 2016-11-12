package mytest.java.util.Vector;

import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * Vector是线程安全的但不是并发的，对于复合操作，对外要加锁保护
 * @author zenghua233
 *
 */
public class MyVectorTest {
	Vector<Integer> vector = new Vector<>();
	
	public MyVectorTest() {
		Collections.addAll(vector, 1,2,3);
	}
	
	/**
	 * 多线程复合操作,Vector是安全的但不可以并发执行
	 * 
	 * 编辑多个单词快捷键 TODO
	 * @throws Exception 
	 */
	@Test
	public void testMixOperate() throws Exception {
		MyVectorTest t = new MyVectorTest();
		ExecutorService exector = Executors.newCachedThreadPool();
		CountDownLatch latch = new CountDownLatch(1);
		exector.execute(new Runnable() {
			@Override
			public void run() {
				t.getLast(vector);
			}
		});
		
		exector.execute(new Runnable() {
			@Override
			public void run() {
				t.delectLast(vector);
			}
		});
		
		latch.await(1000,TimeUnit.SECONDS);
		exector.shutdown();
	}
	
	
	
	
	//==============================================================
	/**
	 * 抛出Array index out of range: 2
	 * @param vector
	 * @return
	 */
	public Integer getLast(Vector<Integer> vector) {
		int index = vector.size() - 1;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return vector.get(index);
	}
	
	public void delectLast(Vector<Integer> vector) {
		int index = vector.size() - 1;
		vector.remove(index);
	}
}
