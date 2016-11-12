package mytest.java.util.concurrent.CyclicBarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyCyclicBarrier {

	public static void main(String args[]) {
		CyclicBarrier cb = new CyclicBarrier(3, new BarrierTask());

		System.out.println("Starting");
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.execute(new TaskA(cb));
		executor.execute(new TaskB(cb));
		executor.execute(new TaskC(cb));
	}
}

class TaskA implements Runnable {
	public CyclicBarrier barrier;

	public TaskA(CyclicBarrier barrier) {
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			System.out.println("TaskA正在等待");
			int i = barrier.await();
			System.out.println("TaskA释放" + i);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}

class TaskB implements Runnable {
	public CyclicBarrier barrier;

	public TaskB(CyclicBarrier barrier) {
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
			System.out.println("TaskB正在等待");
			int i = barrier.await();
			System.out.println("TaskB释放" + i);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}

class TaskC implements Runnable {
	public CyclicBarrier barrier;

	public TaskC(CyclicBarrier barrier) {
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			System.out.println("TaskC正在等待");
			int i = barrier.await();
			System.out.println("TaskC释放" + i);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}

class BarrierTask implements Runnable {

	@Override
	public void run() {
		//当最后一个线程到达屏障时，屏障内置线程最先执行
		System.out.println("BarrierTask is run!");
	}

}
