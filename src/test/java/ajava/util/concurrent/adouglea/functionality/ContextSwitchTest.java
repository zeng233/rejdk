package ajava.util.concurrent.adouglea.functionality;

import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import ajava.util.*;
import ajava.util.concurrent.*;

public final class ContextSwitchTest {
    static final int iters = 1000000;
    static AtomicReference turn = new AtomicReference();
    public static void main(String[] args) throws Exception {
        test();
        test();
        test();
    }

    static void test() throws Exception {
        MyThread a = new MyThread();
        MyThread b = new MyThread();
        a.other = b;
        b.other = a;
        turn.set(a);
        long startTime = System.nanoTime();
        a.start();
        b.start();
        a.join();
        b.join();
        long endTime = System.nanoTime();
        int np = a.nparks + b.nparks;
        System.out.println("Average time: " +
                           ((endTime - startTime) / np) +
                           "ns");
    }

    static final class MyThread extends Thread {

        static {
            // Reduce the risk of rare disastrous classloading in first call to
            // LockSupport.park: https://bugs.openjdk.java.net/browse/JDK-8074773
            Class<?> ensureLoaded = LockSupport.class;
        }

        volatile Thread other;
        volatile int nparks;

        public void run() {
            final AtomicReference t = turn;
            final Thread other = this.other;
            if (turn == null || other == null)
                throw new NullPointerException();
            int p = 0;
            for (int i = 0; i < iters; ++i) {
                while (!t.compareAndSet(other, this)) {
                    LockSupport.park();
                    ++p;
                }
                LockSupport.unpark(other);
            }
            LockSupport.unpark(other);
            nparks = p;
            System.out.println("parks: " + p);
        }
    }
}
