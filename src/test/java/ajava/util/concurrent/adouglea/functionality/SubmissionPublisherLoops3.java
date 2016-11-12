package ajava.util.concurrent.adouglea.functionality;

import ajava.util.*;
import ajava.util.concurrent.*;

/**
 * Creates PRODUCERS publishers each with CONSUMERS subscribers,
 * each sent ITEMS items, with CAP buffering; repeats REPS times
 */
public class SubmissionPublisherLoops3 {/*
    static final int ITEMS      = 1 << 20;
    static final int PRODUCERS  = 32;
    static final int CONSUMERS  = 32;
    static final int CAP        = Flow.defaultBufferSize();
    static final int REPS = 9;

    static final Phaser phaser = new Phaser(PRODUCERS * CONSUMERS + 1);

    public static void main(String[] args) throws Exception {
        int reps = REPS;
        if (args.length > 0)
            reps = Integer.parseInt(args[0]);

        System.out.println("ITEMS: " + ITEMS +
                           " PRODUCERS: " + PRODUCERS +
                           " CONSUMERS: " + CONSUMERS +
                           " CAP: " + CAP);
        for (int rep = 0; rep < reps; ++rep) {
            oneRun();
            Thread.sleep(1000);
        }
    }

    static void oneRun() throws Exception {
        long nitems = (long)ITEMS * PRODUCERS * CONSUMERS;
        long startTime = System.nanoTime();
        for (int i = 0; i < PRODUCERS; ++i)
            new Pub().fork();
        phaser.arriveAndAwaitAdvance();
        long elapsed = System.nanoTime() - startTime;
        double secs = ((double)elapsed) / (1000L * 1000 * 1000);
        double ips = nitems / secs;
        System.out.printf("Time: %7.2f", secs);
        System.out.printf(" items per sec: %14.2f\n", ips);
        System.out.println(ForkJoinPool.commonPool());
    }

    static final class Sub implements Flow.Subscriber<Boolean> {
        int count;
        Flow.Subscription subscription;
        public void onSubscribe(Flow.Subscription s) {
            (subscription = s).request(CAP);
        }
        public void onNext(Boolean b) {
            if (b && (++count & ((CAP >>> 1) - 1)) == 0)
                subscription.request(CAP >>> 1);
        }
        public void onComplete() {
            if (count != ITEMS)
                System.out.println("Error: remaining " + (ITEMS - count));
            phaser.arrive();
        }
        public void onError(Throwable t) { t.printStackTrace(); }
    }

    static final class Pub extends RecursiveAction {
        final SubmissionPublisher<Boolean> pub =
            new SubmissionPublisher<Boolean>(ForkJoinPool.commonPool(), CAP);
        public void compute() {
            SubmissionPublisher<Boolean> p = pub;
            for (int i = 0; i < CONSUMERS; ++i)
                p.subscribe(new Sub());
            for (int i = 0; i < ITEMS; ++i)
                p.submit(Boolean.TRUE);
            p.close();
        }
    }

*/}
