/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.util;

import org.apache.commons.codec.binary.Base64;
import java.security.SecureRandom;

public class KeyGenerator {

    private final static Base64 base64 =  new Base64(11, new byte[]{}, true);
    private final static SecureRandom numberGenerator = new SecureRandom();

    /**
     * Generated 8-byte long randon chars String
     *
     * @return
     */
    public static String generateKey(){
        byte[] randomBytes = new byte[8];
        numberGenerator.nextBytes(randomBytes);

        return new String(base64.encode(randomBytes));
    }

    /**
     * Generated random number from 1 (inlc) to 8 (incl)
     *
     * @return
     */
    public static int generateSeed() {
        return numberGenerator.nextInt(8)+1;
    }

}
