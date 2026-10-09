/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T09:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d5 = Instant.parse("2016-02-17T15:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);//no username
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);//no user name
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "We musn't be ashamed of our dreams @alyssa", d3);//one user name mention
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "Sure @bbitdiddle, what say you @panda?", d4);//two user name mention
    private static final Tweet tweet5 = new Tweet(5, "panda", "Yeah right!!! alyssa@mit!!", d5);//username not counted if @in middle
    
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //partiton 1: 1 tweet, 2+ tweets
    //partition 2: earliet tweet being in the start, mid and end of the list
    
    
    //partiton
    @Test
    public void testGetTimespanOneTweet() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
    	
    	assertEquals("expected start",d1,timespan.getStart());
    	assertEquals("expected end",d1,timespan.getEnd());
    	
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    
    //partiton 2
    @Test
    public void testGetTimespanTwoTweetsWithEarliestAtstart() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanTwoTweetsWithEarliestAtLast() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanTwoTweetsWithEarliestInMiddle() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet3, tweet1));
        
        assertEquals("expected start", d3, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    
    //Partitons: 
    //1. No user name
    //2. One user name
    //3. Two or more username
    //4/ username in wrong notation, @ in middle not counted
    
    
    //partiton case 1
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    //partition case 2
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        
        assertEquals("expected 1",1,  mentionedUsers.size());
    }
    
    //partiton case 3
    @Test
    public void testGetMentionedUsersMoreThanOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        
        assertEquals("expected 2",2,  mentionedUsers.size());
    }
    
    //partiton case 4
    @Test
    public void testGetMentionedUserWrongNotationMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        
        assertEquals("expected 0",0,  mentionedUsers.size());
    }
    

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
