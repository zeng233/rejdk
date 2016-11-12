package ajava.util.concurrent.adouglea.functionality;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.*;

import ajava.util.*;
import ajava.util.concurrent.*;

// parallel sums and cumulations

public class FJSums {
    static int THRESHOLD;
    static final int MIN_PARTITION = 64;

    interface LongByLongToLong { long apply(long a, long b); }

    static final class Add implements LongByLongToLong {
        public long apply(long a, long b) { return a + b; }
    }

    static final Add ADD = new Add();

    public static void main(String[] args) throws Exception {
        int n = 1 << 25;
        int reps = 10;
        try {
            if (args.length > 0)
                n = Integer.parseInt(args[0]);
            if (args.length > 1)
                reps = Integer.parseInt(args[1]);
        }
        catch (Exception e) {
            System.out.println("Usage: java FJSums n reps");
            return;
        }
        int par = ForkJoinPool.getCommonPoolParallelism();
        System.out.println("Number of procs=" + par);
        int p;
        THRESHOLD = (p = n / (par << 3)) <= MIN_PARTITION ? MIN_PARTITION : p;

        long[] a = new long[n];
        for (int i = 0; i < n; ++i)
            a[i] = i;
        long expected = ((long)n * (long)(n - 1)) / 2;
        for (int i = 0; i < reps; ++i) {
            seqTest(a, i, expected);
            parTest(a, i, expected);
        }
        System.out.println(ForkJoinPool.commonPool());
    }

    static void seqTest(long[] a, int index, long expected) {
        System.out.print("Seq ");
        long last = System.nanoTime();
        int n = a.length;
        long ss = seqSum(ADD, 0L, a, 0, n);
        double elapsed = elapsedTime(last);
        System.out.printf("sum = %24d  time:  %7.3f\n", ss, elapsed);
        if (index == 0 && ss != expected)
            throw new Error("expected " + expected + " != " + ss);
    }

    static void parTest(long[] a, int index, long expected) {
        System.out.print("Par ");
        long last = System.nanoTime();
        int n = a.length;
        Summer s = new Summer(null, ADD, 0L, a, 0, n, null);
        s.invoke();
        long ss = s.result;
        double elapsed = elapsedTime(last);
        System.out.printf("sum = %24d  time:  %7.3f\n", ss, elapsed);
        if (index == 0 && ss != expected)
            throw new Error("expected " + expected + " != " + ss);
        System.out.print("Par ");
        last = System.nanoTime();
        new Cumulater(null, ADD, a, 0, n).invoke();
        long sc = a[n - 1];
        elapsed = elapsedTime(last);
        System.out.printf("cum = %24d  time:  %7.3f\n", ss, elapsed);
        if (sc != ss)
            throw new Error("expected " + ss + " != " + sc);
        if (index == 0) {
            long cs = 0L;
            for (int j = 0; j < n; ++j) {
                if ((cs += j) != a[j])
                    throw new Error("wrong element value");
            }
        }
    }

    static double elapsedTime(long startTime) {
        return (double)(System.nanoTime() - startTime) / (1000L * 1000 * 1000);
    }

    static long seqSum(LongByLongToLong fn, long basis,
                       long[] a, int l, int h) {
        long sum = basis;
        for (int i = l; i < h; ++i)
            sum = fn.apply(sum, a[i]);
        return sum;
    }

    /**
     * Cumulative scan, adapted from ParallelArray code
     *
     * A basic version of scan is straightforward.
     *  Keep dividing by two to threshold segment size, and then:
     *   Pass 1: Create tree of partial sums for each segment
     *   Pass 2: For each segment, cumulate with offset of left sibling
     * See G. Blelloch's http://www.cs.cmu.edu/~scandal/alg/scan.html
     *
     * This version improves performance within FJ framework mainly by
     * allowing the second pass of ready left-hand sides to proceed
     * even if some right-hand side first passes are still executing.
     * It also combines first and second pass for leftmost segment,
     * and skips the first pass for rightmost segment (whose result is
     * not needed for second pass).
     *
     * Managing this relies on ORing some bits in the pendingCount for
     * phases/states: CUMULATE, SUMMED, and FINISHED. CUMULATE is the
     * main phase bit. When false, segments compute only their sum.
     * When true, they cumulate array elements. CUMULATE is set at
     * root at beginning of second pass and then propagated down. But
     * it may also be set earlier for subtrees with lo==0 (the left
     * spine of tree). SUMMED is a one bit join count. For leafs, it
     * is set when summed. For internal nodes, it becomes true when
     * one child is summed.  When the second child finishes summing,
     * we then moves up tree to trigger the cumulate phase. FINISHED
     * is also a one bit join count. For leafs, it is set when
     * cumulated. For internal nodes, it becomes true when one child
     * is cumulated.  When the second child finishes cumulating, it
     * then moves up tree, completing at the root.
     *
     * To better exploit locality and reduce overhead, the compute
     * method loops starting with the current task, moving if possible
     * to one of its subtasks rather than forking.
     */
    static final class Cumulater extends CountedCompleter<Void> {
        static final int CUMULATE = 1;
        static final int SUMMED   = 2;
        static final int FINISHED = 4;

        final long[] array;
        final LongByLongToLong function;
        Cumulater left, right;
        final int lo, hi;
        long in, out;

        Cumulater(Cumulater parent, LongByLongToLong function,
                  long[] array, int lo, int hi) {
            super(parent);
            this.function = function; this.array = array;
            this.lo = lo; this.hi = hi;
        }

        public final void compute() {
            final LongByLongToLong fn;
            final long[] a;
            if ((fn = this.function) == null || (a = this.array) == null)
                throw new NullPointerException();    // hoist checks
            int l, h;
            Cumulater t = this;
            outer: while ((l = t.lo) >= 0 && (h = t.hi) <= a.length) {
                if (h - l > THRESHOLD) {
                    Cumulater lt = t.left, rt = t.right, f;
                    if (lt == null) {                // first pass
                        int mid = (l + h) >>> 1;
                        f = rt = t.right = new Cumulater(t, fn, a, mid, h);
                        t = lt = t.left  = new Cumulater(t, fn, a, l, mid);
                    }
                    else {                           // possibly refork
                        long pin = t.in;
                        lt.in = pin;
                        f = t = null;
                        if (rt != null) {
                            rt.in = fn.apply(pin, lt.out);
                            for (int c;;) {
                                if (((c = rt.getPendingCount()) & CUMULATE) != 0)
                                    break;
                                if (rt.compareAndSetPendingCount(c, c|CUMULATE)){
                                    t = rt;
                                    break;
                                }
                            }
                        }
                        for (int c;;) {
                            if (((c = lt.getPendingCount()) & CUMULATE) != 0)
                                break;
                            if (lt.compareAndSetPendingCount(c, c|CUMULATE)) {
                                if (t != null)
                                    f = t;
                                t = lt;
                                break;
                            }
                        }
                        if (t == null)
                            break;
                    }
                    if (f != null)
                        f.fork();
                }
                else {
                    int state; // Transition to sum, cumulate, or both
                    for (int b;;) {
                        if (((b = t.getPendingCount()) & FINISHED) != 0)
                            break outer;                      // already done
                        state = (((b & CUMULATE) != 0)
                                 ? FINISHED
                                 : (l > 0) ? SUMMED : (SUMMED|FINISHED));
                        if (t.compareAndSetPendingCount(b, b|state))
                            break;
                    }

                    long sum = t.in;
                    if (state != SUMMED) {
                        for (int i = l; i < h; ++i)           // cumulate
                            a[i] = sum = fn.apply(sum, a[i]);
                    }
                    else if (h < a.length) {                  // skip rightmost
                        for (int i = l; i < h; ++i)           // sum only
                            sum = fn.apply(sum, a[i]);
                    }
                    t.out = sum;
                    for (Cumulater par;;) {                   // propagate
                        if ((par = (Cumulater)t.getCompleter()) == null) {
                            if ((state & FINISHED) != 0)      // enable join
                                t.quietlyComplete();
                            break outer;
                        }
                        int b = par.getPendingCount();
                        if ((b & state & FINISHED) != 0)
                            t = par;                          // both done
                        else if ((b & state & SUMMED) != 0) { // both summed
                            int nextState; Cumulater lt, rt;
                            if ((lt = par.left) != null &&
                                (rt = par.right) != null)
                                par.out = fn.apply(lt.out, rt.out);
                            int refork = (((b & CUMULATE) == 0 &&
                                           par.lo == 0) ? CUMULATE : 0);
                            if ((nextState = b|state|refork) == b ||
                                par.compareAndSetPendingCount(b, nextState)) {
                                state = SUMMED;               // drop finished
                                t = par;
                                if (refork != 0)
                                    par.fork();
                            }
                        }
                        else if (par.compareAndSetPendingCount(b, b|state))
                            break outer;                      // sib not ready
                    }
                }
            }
        }
    }

    // Uses CC reduction via firstComplete/nextComplete
    static final class Summer extends CountedCompleter<Void> {
        final long[] array;
        final LongByLongToLong function;
        final int lo, hi;
        final long basis;
        long result;
        Summer forks, next; // keeps track of right-hand-side tasks
        Summer(Summer parent, LongByLongToLong function, long basis,
               long[] array, int lo, int hi, Summer next) {
            super(parent);
            this.function = function; this.basis = basis;
            this.array = array; this.lo = lo; this.hi = hi;
            this.next = next;
        }

        public final void compute() {
            final long id = basis;
            final LongByLongToLong fn;
            final long[] a;
            if ((fn = this.function) == null || (a = this.array) == null)
                throw new NullPointerException();
            int l = lo,  h = hi;
            while (h - l >= THRESHOLD) {
                int mid = (l + h) >>> 1;
                addToPendingCount(1);
                (forks = new Summer(this, fn, id, a, mid, h, forks)).fork();
                h = mid;
            }
            long sum = id;
            if (l < h && l >= 0 && h <= a.length) {
                for (int i = l; i < h; ++i)
                    sum = fn.apply(sum, a[i]);
            }
            result = sum;
            CountedCompleter<?> c;
            for (c = firstComplete(); c != null; c = c.nextComplete()) {
                Summer t = (Summer)c, s = t.forks;
                while (s != null) {
                    t.result = fn.apply(t.result, s.result);
                    s = t.forks = s.next;
                }
            }
        }
    }

}
