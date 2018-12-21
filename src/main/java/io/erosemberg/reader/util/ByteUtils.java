package io.erosemberg.reader.util;

/**
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
public class ByteUtils {

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

}
