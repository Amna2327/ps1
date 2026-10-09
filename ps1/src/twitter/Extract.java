/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
import java.util.HashSet;
/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        
        List<Tweet> sorted = new ArrayList<>(tweets);   // copy, so the input isn't modified
        sorted.sort(Comparator.comparing(Tweet::getTimestamp));
        return new Timespan(sorted.get(0).getTimestamp(),sorted.get(sorted.size()-1).getTimestamp());
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentioned = new HashSet<>();
        for (Tweet tweet : tweets) {
            String text = tweet.getText();
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) != '@') continue;
                if (i > 0 && isUsernameChar(text.charAt(i - 1))) continue;

                int end = i + 1;
                while (end < text.length() && isUsernameChar(text.charAt(end))) {
                    end++;
                }
                if (end > i + 1) {
                    mentioned.add(text.substring(i + 1, end).toLowerCase());
                }
            }
        }
        return mentioned;
    }

    private static boolean isUsernameChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

}
