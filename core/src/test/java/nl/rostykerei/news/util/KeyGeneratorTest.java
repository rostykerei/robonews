/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class KeyGeneratorTest {

    private static final Set<Character> VALID_CHARS = new HashSet<Character>(Arrays.asList(
        new Character[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
        }
    ));


    @Test
    public void testGenerateKey() throws Exception {
        for (int i = 0; i< 256; i++) {
            String key = KeyGenerator.generateKey();

            if (key.length() != 11) {
                throw new Exception("Incorrect key has been generated: " + key);
            }

            for (int k = 0; k < 11; k++) {
                if (!VALID_CHARS.contains(key.charAt(k))) {
                    throw new Exception("Incorrect key has been generated: " + key + ", char ["+ key.charAt(k) +"] is unacceptable");
                }
            }
        }

    }

    @Test
    public void testGenerateSeed() throws Exception {
        for (int i = 0; i< 256; i++) {
            int seed = KeyGenerator.generateSeed();

            if (seed < 1 || seed > 8) {
                throw new Exception("Incorrect seed has been generated: " + seed);
            }
        }
    }
}
