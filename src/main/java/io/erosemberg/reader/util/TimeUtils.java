package io.erosemberg.reader.util;

import lombok.experimental.UtilityClass;

import java.util.Date;

/**
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
@UtilityClass
public class TimeUtils {

    private static final long TICKS_AT_EPOCH = 621355968000000000L;
    private static final long TICKS_PER_MILLISECOND = 10000;

    public static Date fromTicks(long ticks) {
        return new Date((ticks - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND);
    }

}
