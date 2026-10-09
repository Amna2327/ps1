/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Testing strategy for writtenBy:
    // Partition on number of matching tweets: none, one, several.
    // Partition on case of the username: matches author exactly, differs in case.

    @Test
    public void testWrittenByNoMatch() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "nobody");

        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");

        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleMatches() {
        Tweet tweet3 = new Tweet(3, "alyssa", "second tweet by alyssa", d2);
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");

        assertEquals("expected two tweets", 2, writtenBy.size());
        assertEquals("expected same order", 0, writtenBy.indexOf(tweet1));
    }

    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");

        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    // Testing strategy for inTimespan:
    // Partition on number of tweets inside the timespan: none, some, all.
    // Partition on position relative to the boundaries: strictly inside, on the start or end boundary (inclusive), outside.

    @Test
    public void testInTimespanNoneInside() {
        Instant testStart = Instant.parse("2016-02-17T13:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T14:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanSomeInside() {
        Instant testStart = Instant.parse("2016-02-17T10:30:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:30:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertEquals("expected one tweet", 1, inTimespan.size());
        assertTrue("expected list to contain tweet2", inTimespan.contains(tweet2));
    }

    @Test
    public void testInTimespanBoundaryInclusive() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(d1, d2));

        assertTrue("expected both tweets at the boundaries", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testInTimespanAllInside() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    // Testing strategy for containing:
    // Partition on number of matching tweets: none, one, several.
    // Partition on number of words searched for: one word, several words (any one may match).
    // Partition on case: word matches text exactly, differs in case.
    // Partition on match type: whole word matches, only a substring matches (not a match).

    @Test
    public void testContainingNoMatch() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("banana"));

        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));

        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("RIVEST"));

        assertEquals("expected both tweets", 2, containing.size());
    }

    @Test
    public void testContainingSeveralWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("minutes", "much?"));

        assertEquals("expected both tweets", 2, containing.size());
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    @Test
    public void testContainingSubstringIsNotAMatch() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("tal"));

        assertTrue("expected empty list, since matches are whole words", containing.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     *
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}