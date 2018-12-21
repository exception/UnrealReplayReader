package io.erosemberg.reader.util;

import lombok.experimental.UtilityClass;

/**
 * Utility class containing all methods with reference to binary data.
 *
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
@UtilityClass
public class ByteUtils {

    // Taken from https://stackoverflow.com/a/29132118
    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

}
