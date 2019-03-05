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
    public static int adjustLength(int length) {
        return length < 0 ? -length * 2 : length;
    }
}
