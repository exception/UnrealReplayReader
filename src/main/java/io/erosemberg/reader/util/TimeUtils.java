package io.erosemberg.reader.util;

import lombok.experimental.UtilityClass;

import java.util.Date;

/**
 * Utility class for any methods to help us interact with time.
 *
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

    public static String msToTimestamp(long ms) {
        long minutes = Math.round(Math.floor(ms / 1000 / 60));
        ms -= minutes * 1000 * 60;
        long seconds = Math.round(Math.floor(ms / 1000));
        ms -= seconds * 1000;
        return (minutes < 10 ? "0" + minutes : String.valueOf(minutes)) + ":" + (seconds < 10 ? "0" + seconds : String.valueOf(seconds));
    }

}
