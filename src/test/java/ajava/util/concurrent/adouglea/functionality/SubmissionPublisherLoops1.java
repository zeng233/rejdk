package ajava.util.concurrent.adouglea.functionality;

import ajava.util.*;
import ajava.util.concurrent.*;

/**
 * One publisher, many subscribers
 */
public class SubmissionPublisherLoops1 {/*
    static final int ITEMS = 1 << 20;
    static final int CONSUMERS  = 64;
    static final int CAP        = Flow.defaultBufferSize();
    static final int REPS = 9;

    static final Phaser phaser = new Phaser(CONSUMERS + 1);

    static final class Sub implements Flow.Subscriber<Boolean> {
        Flow.Subscription sn;
        int count;
        public void onSubscribe(Flow.Subscription s) {
            (sn = s).request(CAP);
        }
        public void onNext(Boolean t) {
            if ((++count & (CAP - 1)) == (CAP >>> 1))
                sn.request(CAP);
        }
        public void onError(Throwable t) { t.printStackTrace(); }
        public void onComplete() {
            if (count != ITEMS)
                System.out.println("Error: remaining " + (ITEMS - count));
            phaser.arrive();
        }
    }

    static final long NPS = (1000L * 1000 * 1000);

    public static void main(String[] args) throws Exception {
        int reps = REPS;
        if (args.length > 0)
            reps = Integer.parseInt(args[0]);

        System.out.println("ITEMS: " + ITEMS +
                           " CONSUMERS: " + CONSUMERS +
                           " CAP: " + CAP);
        ExecutorService exec = ForkJoinPool.commonPool();
        for (int rep = 0; rep < reps; ++rep) {
            oneRun(exec);
            Thread.sleep(1000);
        }
        if (exec != ForkJoinPool.commonPool())
            exec.shutdown();
    }

    static void oneRun(ExecutorService exec) throws Exception {
        long startTime = System.nanoTime();
        final SubmissionPublisher<Boolean> pub =
            new SubmissionPublisher<Boolean>(exec, CAP);
        for (int i = 0; i < CONSUMERS; ++i)
            pub.subscribe(new Sub());
        for (int i = 0; i < ITEMS; ++i) {
            pub.submit(Boolean.TRUE);
        }
        pub.close();
        phaser.arriveAndAwaitAdvance();
        long elapsed = System.nanoTime() - startTime;
        double secs = ((double)elapsed) / NPS;
        System.out.printf("\tTime: %7.3f\n", secs);
    }
*/}
