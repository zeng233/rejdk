package ajava.util.concurrent.adouglea.functionality;

/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import ajava.util.concurrent.*;

/**
 * Recursive task-based version of Fibonacci. Computes:
 * <pre>
 * Computes fibonacci(n) = fibonacci(n-1) + fibonacci(n-2);  for n> 1
 *          fibonacci(0) = 0;
 *          fibonacci(1) = 1.
 * </pre>
 */
public final class Fib extends RecursiveAction {

    // Performance-tuning constant:
    static int sequentialThreshold;

    static long lastStealCount;

    public static void main(String[] args) throws Exception {
        int procs = 0;
        int num = 45;
        sequentialThreshold = 2;
        try {
            if (args.length > 0)
                procs = Integer.parseInt(args[0]);
            if (args.length > 1)
                num = Integer.parseInt(args[1]);
            if (args.length > 2)
                sequentialThreshold = Integer.parseInt(args[2]);
        }
        catch (Exception e) {
            System.out.println("Usage: java Fib <threads> <number> [<sequentialThreshold>]");
            return;
        }

        for (int reps = 0; reps < 2; ++reps) {
            ForkJoinPool g = (procs == 0) ? ForkJoinPool.commonPool() :
                new ForkJoinPool(procs);
            lastStealCount = g.getStealCount();
            for (int i = 0; i < 20; ++i) {
                test(g, num);
                //                if (i == 0)
                //                    Thread.sleep(100);
            }
            System.out.println(g);
            if (g != ForkJoinPool.commonPool())
                g.shutdown();
            Thread.sleep(500);
        }
    }

    /** for time conversion */
    static final long NPS = (1000L * 1000 * 1000);

    static void test(ForkJoinPool g, int num) throws Exception {
        int ps = g.getParallelism();
        long start = System.nanoTime();
        Fib f = new Fib(num);
        g.invoke(f);
        long time = System.nanoTime() - start;
        double secs = ((double)time) / NPS;
        long result = f.getAnswer();
        System.out.print("Fib " + num + " = " + result);
        System.out.printf("\tTime: %7.3f", secs);

        long sc = g.getStealCount();
        long ns = sc - lastStealCount;
        lastStealCount = sc;
        System.out.printf(" Steals/t: %5d", ns/ps);
        System.out.printf(" Workers: %5d", g.getPoolSize());
        System.out.println();
    }

    // Initialized with argument; replaced with result
    int number;

    Fib(int n) { number = n; }

    int getAnswer() {
        return number;
    }

    public final void compute() {
        int n = number;
        if (n > 1) {
            if (n <= sequentialThreshold)
                number = seqFib(n);
            else {
                Fib f1 = new Fib(n - 1);
                Fib f2 = new Fib(n - 2);
                invokeAll(f1, f2);
                number = f1.number + f2.number;
            }
        }
    }

    // Sequential version for arguments less than threshold
    static final int seqFib(int n) { // unroll left only
        int r = 1;
        do {
            int m = n - 2;
            r += m <= 1 ? m : seqFib(m);
        } while (--n > 1);
        return r;
    }

}
