package mytest.java.util.concurrent.ThreadPoolExecutor;

import java.util.concurrent.BlockingQueue;

public class PlainWorkerPool {
	protected final BlockingQueue<Runnable> workQueue;

	public void execute(Runnable r) {
		try {
			workQueue.put(r);
			System.out.println(workQueue.size());
		} catch (InterruptedException ie) { // postpone response
			Thread.currentThread().interrupt();
		}
	}

	public PlainWorkerPool(BlockingQueue<Runnable> ch, int nworkers) {
		workQueue = ch;
		for (int i = 0; i < nworkers; ++i)
			activate();
	}

	protected void activate() {
		Runnable runLoop = new Runnable() {
			public void run() {
				try {
					for (;;) {
						Runnable r = (Runnable) (workQueue.take());
						r.run();
					}
				} catch (InterruptedException ie) {
				} // die
			}
		};
		
		new Thread(runLoop).start();
	}

}
