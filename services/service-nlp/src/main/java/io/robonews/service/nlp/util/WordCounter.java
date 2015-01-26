/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.nlp.util;

public final class WordCounter {

    /**
     * Returns the number of words within input string
     *
     * @param str String
     * @return number of words
     */
    public static int count(String str) {
        if (str != null && str.length() > 0) {
            // TODO there is might be better implementation...
            String[] l = str.replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
            return l.length;
        }

        return 0;
    }

}
