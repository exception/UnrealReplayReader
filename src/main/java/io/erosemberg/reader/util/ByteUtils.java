package io.erosemberg.reader.util;

import com.sun.jna.Native;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

/**
 * Utility class containing all methods with reference to binary data.
 *
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
@UtilityClass
public class ByteUtils {

    public static byte[] decompress(byte[] buffer, int size, int uncompressedSize) {

        OodleLib oodle = Native.load("oo2core_3_win64.dll", OodleLib.class);
        System.out.println("oodle = " + oodle);
        byte[] decompressedBuffer = new byte[uncompressedSize];
        int decompressedCount = oodle.OodleLZ_Decompress(buffer, size, decompressedBuffer, uncompressedSize, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3);
        System.out.println("decompressedCount = " + decompressedCount);

        if (decompressedCount == uncompressedSize) {
            return decompressedBuffer;
        } else if (decompressedCount < uncompressedSize) {
            return Stream.of(buffer).skip(decompressedCount).collect(ByteOutputStream::new, (b, e) -> {
                try {
                    b.write(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, (a, b) -> {}).toByteArray();
        } else {
            throw new IllegalStateException("There was an error while decompressing");
        }
    }

    public static int adjustLength(int length) {
        return length < 0 ? -length * 2 : length;
    }
}
