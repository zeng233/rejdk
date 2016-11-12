package ajava.util.concurrent.adouglea.design;

/*
 * Written by Doug Lea and Martin Buchholz with assistance from
 * members of JCP JSR-166 Expert Group and released to the public
 * domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TimeUnit8Test extends JSR166TestCase {
    public static void main(String[] args) {
        main(suite(), args);
    }

    public static Test suite() {
        return new TestSuite(TimeUnit8Test.class);
    }

    /**
     * tests for toChronoUnit.
     */
//    public void testToChronoUnit() throws Exception {
//        assertSame(ChronoUnit.NANOS,   NANOSECONDS.toChronoUnit());
//        assertSame(ChronoUnit.MICROS,  MICROSECONDS.toChronoUnit());
//        assertSame(ChronoUnit.MILLIS,  MILLISECONDS.toChronoUnit());
//        assertSame(ChronoUnit.SECONDS, SECONDS.toChronoUnit());
//        assertSame(ChronoUnit.MINUTES, MINUTES.toChronoUnit());
//        assertSame(ChronoUnit.HOURS,   HOURS.toChronoUnit());
//        assertSame(ChronoUnit.DAYS,    DAYS.toChronoUnit());
//
//        // Every TimeUnit has a defined ChronoUnit equivalent
//        for (TimeUnit x : TimeUnit.values())
//            assertSame(x, TimeUnit.of(x.toChronoUnit()));
//    }
//
//    /**
//     * tests for TimeUnit.of(ChronoUnit).
//     */
//    public void testTimeUnitOf() throws Exception {
//        assertSame(NANOSECONDS,  TimeUnit.of(ChronoUnit.NANOS));
//        assertSame(MICROSECONDS, TimeUnit.of(ChronoUnit.MICROS));
//        assertSame(MILLISECONDS, TimeUnit.of(ChronoUnit.MILLIS));
//        assertSame(SECONDS,      TimeUnit.of(ChronoUnit.SECONDS));
//        assertSame(MINUTES,      TimeUnit.of(ChronoUnit.MINUTES));
//        assertSame(HOURS,        TimeUnit.of(ChronoUnit.HOURS));
//        assertSame(DAYS,         TimeUnit.of(ChronoUnit.DAYS));
//
//        assertThrows(NullPointerException.class,
//                     () -> TimeUnit.of((ChronoUnit)null));
//
//        // ChronoUnits either round trip to their TimeUnit
//        // equivalents, or throw IllegalArgumentException.
//        for (ChronoUnit cu : ChronoUnit.values()) {
//            final TimeUnit tu;
//            try {
//                tu = TimeUnit.of(cu);
//            } catch (IllegalArgumentException acceptable) {
//                continue;
//            }
//            assertSame(cu, tu.toChronoUnit());
//        }
//    }

}
